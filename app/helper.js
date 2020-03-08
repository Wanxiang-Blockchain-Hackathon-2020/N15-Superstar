/**
 * Copyright 2017 IBM All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the 'License');
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an 'AS IS' BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
'use strict';
var log4js = require('log4js');
var logger = log4js.getLogger('Helper');
logger.setLevel('DEBUG');

var path = require('path');
var util = require('util');

//fabric-client 설명 
/*
-클라이언트 인스턴스는 피어 및 오더러 네트워크와 상호 작용할 수 있는 기본 API표면을 제공합니다.
SDK를 사용하는 애플리케이션은 각각 클라이언트의 별도 인스턴스를 통해 여러 네트워크와 상호 작용해야 할 수 있습니다. 
-client class의 중요한 포인트는 '상태 저장' 이다. 인스턴스는 다음을 사용하여 구성해야 합니다. userContext : 패브릭 벡엔드와 통신하는데 사용할 수 있습니다. userContext는 요청을 서명하는 기능을 캡슐화하는 사용자 클래스의 인스턴스입니다.  
SDK를 다중 사용자 환경에서 사용하는 경우 인증된 사용자 및 클라이언트 인스턴스를 관리하기 위한 두가지 권장 기술이 있다. 
  - 인증된 사용자별로 전용 클랑이언트 인스턴스를 사용합니다. 
    인증된 각 사용자에 대해 새 인스턴스를 생성 합니다. 
    인증된 각 사용자를 별도로 등록하여 각 사용자가 고유한 서명 ID를 얻을수 있습니다
  - 인증된 사용자간에 공유 클라이언트 인스턴스와 공통 서명 ID를 사용합니다. 
*/

var hfc = require('fabric-client');

hfc.setLogger(logger);


// 요청 : var client = await getClientForOrg(userOrg);

async function getClientForOrg (userorg, username) {
	logger.debug('getClientForOrg - ****** START %s %s', userorg, username)
	//이 조직에 대한 연결 프로필이 포함된 패브릭 클라이언트 가져오기
    // get a fabric client loaded with a connection profile for this org
	let config = '-connection-profile-path';

    // 클라이언트 컨텍스트를 구축하고 연결 프로필과 함께 로드
	// 네트워크 설정만 로드하고 나중에 클라이언트를 저장하도록 설정
	// build a client context and load it with a connection profile
	// lets only load the network settings and save the client for later
    // <static>loadFromConfig(loadConfig) : 공통 연결 프로필 개체를 로드하거나 JSON파일을 로드한 후 클라이언트 개체를 반환하십시오.

    // loadFromConfig path : fabric-client/lib/Client.js
    // getConfigSetting path: fabric-ca-client 에서 가져오는걸 볼 수 있다 
    logger.debug('===Start loadFromConfig');
	let client = hfc.loadFromConfig(hfc.getConfigSetting('network'+config));
    // 첫 번째 섹션에 클라이언트 섹션이 없기 때문에 연결 프로필이 현재 연결 프로필의 상단에 로딩되며, 다음 섹션은 실제로 교체되지 않는다.
	// This will load a connection profile over the top of the current one one
	// since the first one did not have a client section and the following one does
	// nothing will actually be replaced.
	// 또한 클라이언트 섹션에 정의된 조직이 정의되어 있으므로 관리자 ID를 설정함
    // This will also set an admin identity because the organization defined in the
	// client section has one defined
    logger.debug('===Start getConfigSetting');
	client.loadFromConfig(hfc.getConfigSetting(userorg+config));
    // 이렇게 하면 연결 프로필의 클라이언트 섹션에 있는 설정에 따라 상태 저장소와 암호화 저장소가 모두 생성됨
	// this will create both the state store and the crypto store based
	// on the settings in the client section of the connection profile
    logger.debug('===Start initCredentialStores');
	await client.initCredentialStores(); //자격 증명 저장소 초기화
    
    // getUserContext 호출은 사용자를 지속성에서 벗어나게 하려고 시도한다.
	// 사용자가 지속성을 유지하도록 저장된 경우, 이는 사용자가 등록 및 등록되었음을 의미한다. 사용자가 지속성이 있는 경우 호출은 사용자를 클라이언트 개체에 할당한다.
	// The getUserContext call tries to get the user from persistence.
	// If the user has been saved to persistence then that means the user has
	// been registered and enrolled. If the user is found in persistence
	// the call will then assign the user to the client object.
    
	if(username) {
        logger.debug('===Start getUserContext');
		let user = await client.getUserContext(username, true);
		if(!user) {
			throw new Error(util.format('User was not found :', username));
		} else {
            var sig =user.getSigningIdentity().sign('test');
            logger.debug('user.sign : %s',sig);
            logger.debug('user._identity : %s',user._identity);
            logger.debug('user_enrollmentSecret : %s',user._enrollmentSecret);
			logger.debug('User %s was found to be registered and enrolled', username);
		}
	}
	logger.debug('getClientForOrg - ****** END %s %s \n\n', userorg, username)
	var kvs=hfc.KeyValueStore;

    logger.debug('key : %o',kvs);
    logger.debug('key : %s',kvs);
    
    return client;
}


//app.js 에서 요청 :let response = await helper.getRegisteredUser(username, orgName, true);
var getRegisteredUser = async function(username, userOrg, isJson) {
	try {
		var client = await getClientForOrg(userOrg);
        //인증 정보 저장소를 성공적으로 초기화함
		logger.debug('Successfully initialized the credential stores');
		    // 클라이언트가 이제 Org1 조직의 에이전트 역할을 수행할 수 있음
			// 사용자가 이미 등록되어 있는지 먼저 확인하십시오.	
            // client can now act as an agent for organization Org1
			// first check to see if the user is already enrolled
		var user = await client.getUserContext(username, true);
		
        if (user && user.isEnrolled()) { //enroll : 명부에 올리다 입학시키다. 
			logger.info('Successfully loaded member from persistence');
		} else {
            // 사용자가 등록되지 않았으므로 등록하려면 관리자 개체가 필요함
			// user was not enrolled, so we will need an admin user object to register
			//%s 사용자가 등록되지 않았으므로 등록하려면 관리자 개체가 필요함
            logger.info('User %s was not enrolled, so we will need an admin user object to register',username);
			var admins = hfc.getConfigSetting('admins');
			let adminUserObj = await client.setUserContext({username: admins[0].username, password: admins[0].secret});
			let caClient = client.getCertificateAuthority();
			let secret = await caClient.register({
				enrollmentID: username,
				affiliation: userOrg.toLowerCase() + '.department1'
                
			}, adminUserObj);
            //사용자에 대한 비밀을 성공적으로 얻음
			logger.debug('Successfully got the secret for user %s',username);
			user = await client.setUserContext({username:username, password:secret});
            //클라이언트 개체에 사용자 이름 %s 및 setUserContext를 성공적으로 등록함
			logger.debug('Successfully enrolled username %s  and setUserContext on the client object', username);
		}
		if(user && user.isEnrolled) {
			if (isJson && isJson === true) {
				var response = {
					success: true,
					secret: user._enrollmentSecret,
					message: username + ' enrolled Successfully',
				};
				return response;
			}
		} else {
			throw new Error('User was not enrolled ');
		}
	} catch(error) {
		logger.error('Failed to get registered user: %s with error: %s', username, error.toString());
		return 'failed '+error.toString();
	}

};


var setupChaincodeDeploy = function() {
	process.env.GOPATH = path.join(__dirname, hfc.getConfigSetting('CC_SRC_PATH'));
};

var getLogger = function(moduleName) {
	var logger = log4js.getLogger(moduleName);
	logger.setLevel('DEBUG');
	return logger;
};


exports.getClientForOrg = getClientForOrg;
exports.getLogger = getLogger;
exports.setupChaincodeDeploy = setupChaincodeDeploy;
exports.getRegisteredUser = getRegisteredUser;

