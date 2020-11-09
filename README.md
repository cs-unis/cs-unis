### Introduction

​	Consensus Unis Consensus decentralized trade by Unis Early DAO team initiatives. Through the four-chain combined cycle destruction mechanism, to build consensus, public Finance, prosperity and sharing system.Build a million Consensus Unis shareholding plan.

​	Consensus Unis is operated from the bottom through four public chains, con, sen,sus and unis.Protocol link, all contracts can be checked on the chain, GitHub full open source, farewell to the era of tokens.Don't Rely on any public chain, through their own agreements, to maintain the overall order.

Consensus Unis is divided into four stages:

​	Stage1:  consensus (drainage)

​		Code: CON. 


​		Number of issued:  7 million pieces. 
​		Issue price:  0.08USDT. 
​		Income logic:  USDT buys miners to produce CON.
​			       The number of output is 7 million x 51% x 50% = 1.785 million.
​			       Turn on CON to buy mining machine to produce CON.
​			       The number of output is 7 million x 51% x 50% = 1.785 million.
​		Total output:  the output reaches 51%, which is 3.57 million stop mining,Start SEN mining. 

​	Stage2:  Communion (development)

​		Code:  SEN. 
​		Number of issued:  6 million. 
​		Issue price:  0.08USDT. 
​		Profit logic:  CON purchases miners to produce SEN.
​			       Output quantity 6 million x 51% x 50% = 1.53 million.
​			       Turn on 50%CON+50%SEN to buy miners to produce SEN.
​			       Output quantity 6 million x 51% x 50% = 1.53 million.
​		Total output:  the output reaches 51%, which is 3.06 million stop mining,Start SUS mining. 

​	Stage3:  Wealth together (breakthrough)

​		Code:  SUS. 
​		Number of issued:  5 million pieces. 
​		Issue price:  0.08USDT. 
​		Profit logic:  50%CON+50%SEN purchase mining machine to produce SUS.
​			       Output quantity 5 million x 51% x 50% = 1.275 million.
​			       Turn on 20%CON+30%SEN+50%SUS to purchase mining machines and produce SUS.
​			       Output quantity 5 million x 51% x 50% = 1.275 million.
​		Total output:  the output reaches 51%, which is 2.55 million stop mining,Open UNIS mining. 

​	Stage4: Sharing (prosperity)

​		Code:  UNIS. 
​		Number of issued:  4 million pieces. 
​		Issue price:  0.08USDT. 
​		Profit logic:  20%CON+30%SEN+50%SUS purchase mining machine to produce UNIS.
​			       Output quantity 4 million x 51% x 50% = 1.02 million. 
​		Total output:  the output reaches 25.5%, which is 1.02 million stop mining,Open the exchange ecosystem, UNIS will serve as The only ecological asset of the 	                                Consensus Unis platform. 

### Single node deployment

​	__This is very simple, just run like a normal SpringBoot project. The configuration of the node1 node used by the single node by default__

### Multi-node deployment

​	The project deploys 3 nodes by default and creates 3 configuration files ：application-{env}.yml , If you want to deploy more nodes, create more configuration files and it's OK.

​	It is very simple to use idea to deploy and test. Follow the method below to add multiple springBoot startup configurations.

​	Then start 3 nodes separately. After startup, the nodes are automatically connected to a P2P network, and then you can use the postman tool to test. If you don't have postman installed, please install it yourself, or use the postman extension of the chrome browser like me.

### Web test API

|      API name       | Request method |           URL           |
| :-----------------: | :------------: | :---------------------: |
|   Generate wallet   |      POST      |      /account/new       |
|  View wallet list   |      GET       |      /account/list      |
|    Start mining     |      GET       |       /chain/mine       |
|  Send transaction   |      POST      | /chain/transactions/new |
| View the last block |      GET       |    /chain/block/view    |
|      Add node       |      POST      |     /chain/node/add     |
|      View node      |      GET       |    /chain/node/view     |

> Note: All POST requests use RequestBody to pass parameters, not the form-data form of the form. For example, the parameter form of sending transaction is as follows.
>
> ```json
> {
>     "name" : "value",
>     "name2" : "value2"
> }
> ```

### Simple test

​	First, start the node1-node3 3 nodes in turn. Since the initialized nodes will be automatically linked when they are started, each of them will be connected to a P2P network, so when the linked node is not started, a network exception will be thrown, don't worry about it, etc. After other nodes are started, they will be automatically connected.

> Create a wallet account   http://127.0.0.1:8081/account/new

__The response is as follows:__

```json
{
    "code": 200,
    "message": "New account created, please remember your Address and Private Key.",
    "item": {
        "address": "0xd7dd662e41c66ae31e493ca719a1d1e04a8174fe",
        "balance": 0,
        "privateKey": "aaaf41772d195fd61a236871a0e100589efaceee9f33c12491cb37e99b9a165d"
    }
}
```

> Mining, you must create a mining wallet before mining, the operation is the same as above.
>
> http://127.0.0.1:8081/chain/mine

__The response is as follows:__

```json
{
    "code": 200,
    "message": "Create a new block",
    "item": {
        "header": {
            "index": 8,
            "difficulty": 2.8269553036454149273332760011886696253239742350009903329945699220681916416e+73,
            "nonce": 9302,
            "timestamp": 1531739951847,
            "hash": "0002d041d584afb3609bfeb58a1eb25bef5540154f6b672522ce6e455c08c75b",
            "previousHash": "000c3738e3819bb52fc254e185eaae00dd39086a8bc2837cb4faf06d6edc51d6"
        },
        "body": {
            "transactions": [
                {
                    "from": null,
                    "sign": null,
                    "to": "0x69dc11cae775647d495b2c8930a17b31827b286b",
                    "publicKey": null,
                    "amount": 50,
                    "timestamp": 1531739951847,
                    "txHash": "0x472f84eb7488d4b72a5dc4c6b40b496dfa2b281c655fd2d4d1fefbd047b7fbda",
                    "status": "SUCCESS",
                    "errorMessage": null,
                    "data": "Miner Reward."
                }
            ]
        }
    }
}
```

> Send transaction   http://127.0.0.1:8081/chain/transactions/new

__The request method is POST, and the parameters are as follows:__

```json
{
	"from":"0x69dc11cae775647d495b2c8930a17b31827b286b",
	"to":"0x9f44d5aa11ba82b6e2cfe47ef529f8eabc6ebda5",
	"amount":5.5,
	"privateKey":"69c4caa1495e678208f1ee60f578a63ce5d0a6780541877454545a722175d760",
	"data":"hello world"
}
```

__The response is as follows:__

```json
{
    "code": 200,
    "message": "SUCCESS",
    "item": {
        "from": "0x69dc11cae775647d495b2c8930a17b31827b286b",
        "sign": "3045022100AE0606BACCDAFCBA8B84ED27B58B5F0239F243DEAFF46617E56864A6D8A677E702204DE4EBAC8213225D68D6395FD54602FCF24CD71D96E82F21DBEF77CADC43F70F",
        "to": "0x9f44d5aa11ba82b6e2cfe47ef529f8eabc6ebda5",
        "publicKey": "PZ8Tyr4Nx8MHsRAGMpZmZ6TWY63dXWSCz7FbyM3mvg3favYhhHXarHN6hXgYwKtvLAfXM5YgLDnZx1YPoo4G9pdiR5RQrhtBYriMCh5mGC3RC93HLFkBnAgi",
        "amount": 5.5,
        "timestamp": 1531739924777,
        "txHash": "0x74e5e59d69604b4081c81dae66f429febea9abb77ed6cd7f5b33e6da8ae667f9",
        "status": "APPENDING",
        "errorMessage": null,
        "data": "hello world"
    }
}
```

> View account list   http://127.0.0.1:8081/account/list

__The response is as follows:__

```json
{
    "code": 200,
    "message": "SUCCESS",
    "item": [
        {
            "address": "0x230ee512f454c4cb90e54b730d52a73e726b6db1",
            "balance": 0
        },
        {
            "address": "0x69dc11cae775647d495b2c8930a17b31827b286b",
            "balance": 269
        },
        {
            "address": "0x800c9be60dcec723525432338944dc6a7a8a9bc4",
            "balance": 0
        },
        {
            "address": "0x9f44d5aa11ba82b6e2cfe47ef529f8eabc6ebda5",
            "balance": 5.5
        },
        {
            "address": "0xd7dd662e41c66ae31e493ca719a1d1e04a8174fe",
            "balance": 0
        }
    ]
}
```

> View block   http://127.0.0.1:8081/chain/block/view

__The response is as follows:__

```json
{
    "code": 200,
    "message": "SUCCESS",
    "item": {
        "header": {
            "index": 9,
            "difficulty": 2.8269553036454149273332760011886696253239742350009903329945699220681916416e+73,
            "nonce": 18508,
            "timestamp": 1531740497326,
            "hash": "00059e0981bcd08ed4fbaf8973a738a2111ab5887c54e4b685579658cb4bb38c",
            "previousHash": "0002d041d584afb3609bfeb58a1eb25bef5540154f6b672522ce6e455c08c75b"
        },
        "body": {
            "transactions": [
                {
                    "from": null,
                    "sign": null,
                    "to": "0x69dc11cae775647d495b2c8930a17b31827b286b",
                    "publicKey": null,
                    "amount": 50,
                    "timestamp": 1531740497326,
                    "txHash": "0xd48edd85006ae5331fb934b0236944eb5f87761a3784582cd3dd03b793d17e5a",
                    "status": "SUCCESS",
                    "errorMessage": null,
                    "data": "Miner Reward."
                },
                {
                    "from": "0x69dc11cae775647d495b2c8930a17b31827b286b",
                    "sign": "30450220644FC1CAA4342AB7AFBEF200DA80E6870BBB9C5D3638CCE14635713B4E88BA80022100CA60B42FBDD6767E9605E005296499D682525D429BF0ACEECB450B826510534E",
                    "to": "0x9f44d5aa11ba82b6e2cfe47ef529f8eabc6ebda5",
                    "publicKey": "PZ8Tyr4Nx8MHsRAGMpZmZ6TWY63dXWSCz7FbyM3mvg3favYhhHXarHN6hXgYwKtvLAfXM5YgLDnZx1YPoo4G9pdiR5RQrhtBYriMCh5mGC3RC93HLFkBnAgi",
                    "amount": 5.5,
                    "timestamp": 1531740497291,
                    "txHash": "0x2a06176017345522882bbf6a6e5c4247ecdfc49fc705edab6e820f88af89add6",
                    "status": "APPENDING",
                    "errorMessage": null,
                    "data": "hello world"
                }
            ]
        }
    }
}
```
