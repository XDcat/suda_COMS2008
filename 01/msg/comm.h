#ifndef _FOMM_H_
#define _COMM_H_

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/ipc.h>
#include <sys/types.h>
#include <sys/msg.h>

struct msgbuf
{
	long mtype;
	char mtext[1024];
};

#define SERVER_TYPE 1
#define CLIENT_TYPE 2

int createMsgQuene();  // 创建
int getMsgeQuene();  // 获取
int destoryMsgQueue(int msg_id);  // 销毁
int sendMsgQueue(int msg_id, int who, char* msg);  // 发送
int recvMsgQueue(int msg_id, int recvType, char out[]);  // 接收
#endif
