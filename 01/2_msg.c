// 实验 1-2 通过消息队列，实现进程间的通讯
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/ipc.h>
#include <sys/types.h>
#include <sys/msg.h>

// 消息结构体
struct msgbuf
{
	long mtype;  // 必须以长整型开始
	char mtext[1024];  // 内容
};

int main(void)
{
	key_t key;  // 标识符
	int msg_id;  // 消息 id
	pid_t pid;  // 进程 id
	int msg_count = 1;  // 给 msg 计数

	// 创建消息队列
	key = ftok("/tmp", 2);  // 创建标识符
	// 判断是否生成 key
	if (key < 0)
	{
		// 如果失败，退出
		perror("ftok");
		return -1;
	}
	
	// 创建消息队列（如果不存在则创建，如果已经存在则报错），并获取id
	msg_id = msgget(key, IPC_CREAT|IPC_EXCL|0666);	
	if (msg_id < 0)
	{
		// 当 msg 已经存在或创建失败时，返回 -1
		perror("msgget");
			return -1;
	}
	printf("------ 开始：测试 10 条消息 ------\n");
	printf("------ 创建消息队列 %d ------\n", msg_id);
	// 创建 生产者 和 消费者进程
	// 父进程：生产者
	// 子进程：消费者
	pid = fork();
	if (pid < 0)
	{
		// 创建进程出错，退出
		perror("fork");
		return -1;
	} else if (pid == 0)
	{
		// 子进程 -》消费者
		// 循环消费消息
		int count_rcv = 0;
		while(++count_rcv <= 10)
		{
		struct msgbuf rcv_buf;
		if (msgrcv(msg_id, (void*)&rcv_buf, sizeof(rcv_buf.mtext), 1, 0) < 0)
		{
			// 当返回值为 -1 时，接受失败
			perror("msgrcv");
			return -1;
		}

		printf("-接收消息: %s\n", rcv_buf.mtext);

		sleep(random() % 5);  // 随机暂停几秒
		}
		
		// 删除队列
		msgctl(msg_id, IPC_RMID, NULL);
		printf("------ 结束：删除消息队列 %d ------\n", msg_id);

	} else 
	{
		// 父进程 -》生产者
		// 循环生产消息
		while (msg_count <= 10)
		{
			struct msgbuf send_buf;
			send_buf.mtype = 1;  // 接收者以此确定消息类型

			// send_buf.mtext = "发送消息啦";  // 发送消息的内容
			// strcpy(send_buf.mtext, "发消息啦");  // 使用 strcpy 给数组赋值
			sprintf(send_buf.mtext, "消息%d", msg_count++); 

			if (msgsnd(msg_id, (void*)&send_buf, sizeof(send_buf.mtext), 0) < 0)
			{
				// 当返回值为 -1 时，发送失败
				perror("msgsnd");
				return -1;
			}
			printf("+发送消息：%s\n", send_buf.mtext);

			sleep(random() % 3);  // 随机暂停几秒
		}

		// 等待子进程结束, 防止命令行错乱
		wait(NULL);
	}
	


	return 0;
}
