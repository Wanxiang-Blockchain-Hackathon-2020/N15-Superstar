package com.example.chinacompetition.blockchain;

public class Peers {

    String peers0 ="peer0.org1.example.com";
    String peers1 ="peer0.org2.example.com";

    public Peers(String peers0, String peers1) {
        this.peers0 = peers0;
        this.peers1 = peers1;
    }

    public String getPeers0() {
        return peers0;
    }

    public void setPeers0(String peers0) {
        this.peers0 = peers0;
    }

    public String getPeers1() {
        return peers1;
    }

    public void setPeers1(String peers1) {
        this.peers1 = peers1;
    }
}
