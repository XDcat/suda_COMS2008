// 实验 1-2 通过消息队列，实现进程间的通讯
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/ipc.h>
#include <sys/types.h>
#include <sys/msg.h>


int main(void)
{
	key_t key;  // 标识符
	int msg_id;  // 消息 id
	pid_t pid;  // 进程 id

	// 创建消息队列
	key = ftok("/tmp", 2);  // 创建标识符
	if (key < 0)
	{
		perror("ftok");
		return -1;
	}

	msg_id = msgget(key, IPC_CREAT|IPC_EXCL|0666);	// 创建消息队列，并获取id
	if (msg_id < 0)
	{
		// 当 msg 已经存在或者创建失败时，返回 -1
		perror("msgget");
			return -1;
	}

	// 创建 生产者 和 消费者进程
	// 父进程：生产者
	// 子进程：消费者
	pid = fork();
	if (pid < 0)
	{
		perror("fork");
		return -1;
	} else if (pid == 0)
	{
		// 子进程 -》消费者
		// 消费消息
		struct msgbuf rcv_buf;
		if (msgrcv(msg_id, (void*)&rcv_buf, sizeof(rcv_buf.mtext), 1, 0) < 0)
		{
			// 当返回值为 -1 时，接受失败
			perror("msgrcv");
			return -1;
		}
		printf("接收到消息: %c", rcv_buf.mtext);
	} else 
	{
		// 父进程 -》生产者
		// 生产消息
		struct msgbuf send_buf;
		send_buf.mtype = 1;  // 接收者以此确定消息类型
		send_buf.mtext = "发送消息啦";  // 发送消息的内容

		if (msgsnd(msg_id, (void*)&send_buf, sizeof(send_buf.mtext), 0) < 0)
		{
			// 当返回值为 -1 时，发送失败
			perror("msgsnd");
			return -1;
		}
	}
	
	// 删除队列
	if (msgctl(msg_id, IPC_RMID, NULL) < 0)
	{
		perror("msgctl");
		return -1;
	}
	return 0;


}
