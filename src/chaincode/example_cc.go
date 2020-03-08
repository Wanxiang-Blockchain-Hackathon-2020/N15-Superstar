
package main

import (
	"fmt"
	"strconv"
    "encoding/json"
	"github.com/hyperledger/fabric/core/chaincode/shim"
	pb "github.com/hyperledger/fabric/protos/peer"
    "bytes"
)
// SimpleChaincode example simple Chaincode implementation
type SimpleChaincode struct {
}


type WalletInfo struct {
    Id string `json:"id"`
    Cash int `json:"cash,string"`
}

type ContractInfo struct {
    Title string `json:"title"`
    Content string `json:"content"`
    ClientId string `json:"clientid"`
    FreelanceId string `json:"freelanceid"`
    ClientSign string `json:"clientsign"`
    FreelanceSign string `json:"freelancesign"`
    StartDay string `json:"startday"`
    EndDay string `json:"endday"`
    Cash int `json:"cash,string"`
    State string `json:"state"`
}

type ReviewInfo struct {
    Identi string `json:"identi"`    
    Title string `json:"title"`
    Content string `json:"content"`
    TargetId string `json:"targetid"`
    WritId string `json:"writid"`
    Day string `json:"day"`
}

func (t *SimpleChaincode) Init(stub shim.ChaincodeStubInterface) pb.Response {
    fmt.Println("Chaincode Init")
	return shim.Success(nil)
}

func (t *SimpleChaincode) Invoke(stub shim.ChaincodeStubInterface) pb.Response {
	fmt.Println("my ex02 Invoke")
	function, args := stub.GetFunctionAndParameters()
	if function == "makeIdAndCash" {
		// Make payment of X units from A to B
		return t.makeIdAndCash(stub, args)
	}  else if function == "queryAll" {
		// the old "Query" is now implemtned in invoke
		return t.queryAll(stub, args)
	} else if function == "query" {
		// the old "Query" is now implemtned in invoke
		return t.query(stub, args)
	}else if function == "moveCash" {
        return t.moveCash(stub, args)
    } else if function == "queryById" {
        return t.queryById(stub, args)
    } else if function == "makeContract"{
        return t.makeContract(stub,args)
    } else if function == "editContract"{
        return t.editContract(stub,args)
    } else if function == "Withdrawal"{
        return t.Withdrawal(stub,args)
    } else if function == "Deposit"{
        return t.Deposit(stub,args)
    } else if function == "MakeReview"{
        return t.MakeReview(stub,args)
    }

	return shim.Error("Invalid invoke function name.")
}

func (t *SimpleChaincode) MakeReview(stub shim.ChaincodeStubInterface, args []string) pb.Response {
    identi := args[0]
    title := args[1]
    content := args[2]
    targetid := args[3]
    writid := args[4]
    day := args[5]
    
    reviewInfo := &ReviewInfo{identi, title, content, targetid, writid, day}
    reviewInfoBytes, err := json.Marshal(reviewInfo)
    if err != nil {
        return shim.Error(err.Error())
    }

    //블록체인 네트워크에 데이터 기록 
    var keyName string="review" + identi
    err = stub.PutState(keyName,reviewInfoBytes)
    if err != nil {
        return shim.Error(err.Error())
    }
    fmt.Println("put  complete")
    return shim.Success(nil)
}

func (t *SimpleChaincode) makeContract(stub shim.ChaincodeStubInterface, args []string) pb.Response {
    title := args[0]
    content := args[1]
    var clientId string= args[2]
    var freelanceId string= args[3]
    clientSign := args[4]
    freelanceSign := args[5]
    var startDay string= args[6]
    endDate := args[7]
    cash,err := strconv.Atoi(args[8])
    if err != nil{
        return shim.Error(err.Error())
    }
    state := args[9]
    
    contractInfo := &ContractInfo{title, content, clientId, freelanceId, clientSign, freelanceSign,startDay, endDate, cash, state}
    contractInfoBytes, err := json.Marshal(contractInfo)
    if err != nil {
        return shim.Error(err.Error())
    }

    //블록체인 네트워크에 데이터 기록 
    var keyName string= clientId  + freelanceId+startDay
    err = stub.PutState(keyName,contractInfoBytes)
    if err != nil {
        return shim.Error(err.Error())
    }
    fmt.Println("put contract complete")
    return shim.Success(nil)
}
func (t *SimpleChaincode) editContract(stub shim.ChaincodeStubInterface, args []string) pb.Response {
    //키값을 받아 
    key := args[0]
    tableName := args[1]
    editText := args[2]
    
    //키값을 조회해서 객체를 찾아본다 
    keyBytes, err := stub.GetState(key)
    if err != nil{
        return shim.Error("Failed to get fromId:"+err.Error())
    }else if keyBytes == nil{
        return shim.Error("Data does not exist")
    }
    
    //json 객체에 데이터를 넣어준다
    contractInfo := ContractInfo{}
    err=json.Unmarshal(keyBytes,&contractInfo)
    if err != nil{
        return shim.Error(err.Error())
    }
    
    //데이터를 수정한다
    if tableName == "title" {
        contractInfo.Title = editText
        
    }else if tableName == "content" {
        contractInfo.Content = editText
        
    }else if tableName == "clientid" {
        contractInfo.ClientId = editText
        
    }else if tableName == "freelanceid" {
        contractInfo.FreelanceId = editText
        
    }else if tableName == "clientsign" {
        contractInfo.ClientSign = editText
        
    }else if tableName == "freelancesign" {
        contractInfo.FreelanceSign = editText
        
    }else if tableName == "startday" {
        contractInfo.StartDay = editText
        
    }else if tableName == "endday" {
        contractInfo.EndDay = editText
        
    }else if tableName == "cash" {
        X, err := strconv.Atoi(editText)
        if err != nil {
            return shim.Error("Invalid transaction amount, expecting a integer value")
        }
        contractInfo.Cash = X
        
    }else if tableName == "state" {
        contractInfo.State = editText
        
    }else {
        return shim.Error("바꾸려는 테이블명이 올바르지 않습니다")
    }

    //수정한 데이터를 저장한다 
    contractBytes, _ := json.Marshal(contractInfo)
    err = stub.PutState(key, contractBytes)
    if err != nil { 
        return shim.Error(err.Error())
    }
    
    return shim.Success(nil)

}
func (t *SimpleChaincode) makeIdAndCash(stub shim.ChaincodeStubInterface, args []string) pb.Response {
    id := args[0]
    //생성시 보유금
    val, err := strconv.Atoi(args[1])
    fmt.Println("log>Input Id Cash : "+id)
    valInfo := &WalletInfo{id, val}
    valInfoBytes, err := json.Marshal(valInfo)
    if err != nil {
        return shim.Error(err.Error())
    }
    
    //블록체인 네트워크에 데이터 기록 
    err = stub.PutState(id,valInfoBytes)
    if err != nil {
        return shim.Error(err.Error())
    }
    fmt.Println("putState complete")
    return shim.Success(nil)
}

func (t *SimpleChaincode) moveCash(stub shim.ChaincodeStubInterface, args []string) pb.Response {
    fmt.Println("call moveCash Chaincode")
    
    from := args[0]
    to := args[1]
    X, err := strconv.Atoi(args[2])
    if err != nil {
        return shim.Error("Invalid transaction amount, expecting a integer value")
    }
    
    FromBytes, err := stub.GetState(from)
    if err != nil{
        return shim.Error("Failed to get fromId:"+err.Error())
    }else if FromBytes == nil{
        return shim.Error("Data does not exist")
    }
    
    toBytes, err := stub.GetState(to)
    if err != nil{
        return shim.Error("Failed to get fromId:"+err.Error())
    }else if FromBytes == nil{
        return shim.Error("Data does not exist")
    }
    
    fromCash := WalletInfo{}
    err=json.Unmarshal(FromBytes,&fromCash)
    if err != nil{
        return shim.Error(err.Error())
    }
    
    toCash := WalletInfo{}
    err = json.Unmarshal(toBytes,&toCash)
    if err != nil{
        return shim.Error(err.Error())
    }
    
    if fromCash.Cash >= X {
        fromCash.Cash = fromCash.Cash - X
        toCash.Cash = toCash.Cash + X
    }else {
        return shim.Error("be short of money")
    }

    
    fromCashBytes, _ := json.Marshal(fromCash)
    err = stub.PutState(from, fromCashBytes)
    if err != nil { 
        return shim.Error(err.Error())
    }
    
    toCashBytes, _ := json.Marshal(toCash)
    err = stub.PutState(to, toCashBytes)
    if err != nil { 
        return shim.Error(err.Error())
    }
    
    return shim.Success(nil)
}

//입금
func (t *SimpleChaincode) Deposit(stub shim.ChaincodeStubInterface, args []string) pb.Response {
    fmt.Println("call Deposit Chaincode")
    
    id := args[0]
    X, err := strconv.Atoi(args[1])
    if err != nil {
        return shim.Error("Invalid transaction amount, expecting a integer value")
    }
    
    idBytes, err := stub.GetState(id)
    if err != nil{
        return shim.Error("Failed to get fromId:"+err.Error())
    }else if idBytes == nil{
        return shim.Error("Data does not exist")
    }
    
    idCash := WalletInfo{}
    err=json.Unmarshal(idBytes,&idCash)
    if err != nil{
        return shim.Error(err.Error())
    }
    
    idCash.Cash = idCash.Cash + X
    
    idCashBytes, _ := json.Marshal(idCash)
    err = stub.PutState(id, idCashBytes)
    if err != nil { 
        return shim.Error(err.Error())
    }
    
    return shim.Success(nil)
}

//출금
func (t *SimpleChaincode) Withdrawal(stub shim.ChaincodeStubInterface, args []string) pb.Response {
    fmt.Println("call Withdrawal Chaincode")
    
    id := args[0]
    X, err := strconv.Atoi(args[1])
    if err != nil {
        return shim.Error("Invalid transaction amount, expecting a integer value")
    }
    
    idBytes, err := stub.GetState(id)
    if err != nil{
        return shim.Error("Failed to get fromId:"+err.Error())
    }else if idBytes == nil{
        return shim.Error("Data does not exist")
    }
    
    idCash := WalletInfo{}
    err=json.Unmarshal(idBytes,&idCash)
    if err != nil{
        return shim.Error(err.Error())
    }
    
    if idCash.Cash >= X {
        idCash.Cash = idCash.Cash - X    
    }else {
        return shim.Error("be short of money")
    }
    
    
    idCashBytes, _ := json.Marshal(idCash)
    err = stub.PutState(id, idCashBytes)
    if err != nil { 
        return shim.Error(err.Error())
    }
    
    return shim.Success(nil)
}


func (t *SimpleChaincode) query(stub shim.ChaincodeStubInterface, args []string) pb.Response {
    fmt.Println("call query method")

    if len(args)%2 == 1 {
        return shim.Error("args의 개수가 짝수가 아닙니다")
    }else {
        if len(args) == 0 {
            return shim.Error("입력된 args가 없습니다")
        }
    }
    var queryString string =""

    if len(args) == 2{
        queryString = fmt.Sprintf("{\"selector\":{\"%s\":\"%s\"}}", args[0], args[1])
    }else if len(args) == 4{
        queryString = fmt.Sprintf("{\"selector\":{\"%s\":\"%s\",\"%s\":\"%s\"}}", args[0], args[1],args[2], args[3])
    }else if len(args) == 6{
        queryString = fmt.Sprintf("{\"selector\":{\"%s\":\"%s\",\"%s\":\"%s\",\"%s\":\"%s\"}}", args[0], args[1],args[2], args[3],args[4], args[5])
    }else if len(args) == 8{
        queryString = fmt.Sprintf("{\"selector\":{\"%s\":\"%s\",\"%s\":\"%s\",\"%s\":\"%s\",\"%s\":\"%s\"}}", args[0], args[1],args[2], args[3],args[4], args[5],args[6], args[7])
    }else if len(args) == 10{
        queryString = fmt.Sprintf("{\"selector\":{\"%s\":\"%s\",\"%s\":\"%s\",\"%s\":\"%s\",\"%s\":\"%s\",\"%s\":\"%s\"}}", args[0], args[1],args[2], args[3],args[4], args[5],args[6], args[7],args[8], args[9])
    } else {
    	return shim.Error("5개 이상의 쿼리를 지원하지 않습니다")
    }
    fmt.Println("queryString"+queryString)
    queryResults, err := getQueryResultForQueryString(stub,queryString)
    if err != nil{
        return shim.Error(err.Error())
    }
    return shim.Success(queryResults)
}

func (t *SimpleChaincode) queryAll(stub shim.ChaincodeStubInterface, args []string) pb.Response {
    fmt.Println("call query method")
    queryString := "{\"selector\":{}}"
    fmt.Println("queryString"+queryString)
    
    queryResults, err := getQueryResultForQueryString(stub,queryString)
    if err != nil{
        return shim.Error(err.Error())
    }
    return shim.Success(queryResults)
}

func (t *SimpleChaincode) queryById(stub shim.ChaincodeStubInterface, args []string) pb.Response {
    id := args[0]
    queryString := fmt.Sprintf("{\"selector\":{\"id\":\"%s\"}}", id)
    fmt.Println("queryString"+queryString)
    
    queryResults, err := getQueryResultForQueryString(stub,queryString)
    if err != nil{
        return shim.Error(err.Error())
    }
    return shim.Success(queryResults)
}

func getQueryResultForQueryString(stub shim.ChaincodeStubInterface, queryString string)([]byte, error){
    resultsIterator, err := stub.GetQueryResult(queryString)
    if err != nil{
        return nil, err
    }
    
    //defer=> 특정 문자 혹은 함수를 나중에(defer를 호출하는 함수가 리턴하기 직전에)실행하게 한다. java의 finally 블록처럼 마지막에 Clean-up 작업을 위해 사용된다.  에러가 발생하더라도 Close할 수 있음
    defer resultsIterator.Close()
    buffer, err := constructQueryResponseFromIterator(resultsIterator)
    if err != nil{
        return nil, err
    }
    return buffer.Bytes(), nil
}

// ===========================================================================================
// constructQueryResponseFromIterator constructs a JSON array containing query results from
// a given result iterator
// ===========================================================================================
func constructQueryResponseFromIterator(resultsIterator shim.StateQueryIteratorInterface) (*bytes.Buffer, error) {
	// buffer is a JSON array containing QueryResults
	var buffer bytes.Buffer
	buffer.WriteString("[")

	bArrayMemberAlreadyWritten := false
	for resultsIterator.HasNext() {
		queryResponse, err := resultsIterator.Next()
		if err != nil {
			return nil, err
		}
		// Add a comma before array members, suppress it for the first array member
		if bArrayMemberAlreadyWritten == true {
			buffer.WriteString(",")
		}
		buffer.WriteString("{\"Key\":")
		buffer.WriteString("\"")
		buffer.WriteString(queryResponse.Key)
		buffer.WriteString("\"")

		buffer.WriteString(", \"Record\":")
		// Record is a JSON object, so we write as-is
		buffer.WriteString(string(queryResponse.Value))
		buffer.WriteString("}")
		bArrayMemberAlreadyWritten = true
	} //end for
	buffer.WriteString("]")

	return &buffer, nil
}



func main() {
	err := shim.Start(new(SimpleChaincode))
	if err != nil {
		fmt.Printf("Error starting Simple chaincode: %s", err)
	}
}
