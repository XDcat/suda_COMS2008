#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <string.h>
#include <sys/shm.h>
#include <sys/types.h>
#include "common.h"

int main()
{
	pid_t pid;  // fork 后的返回值
	int mid;  // 共享内存的 id
	char this[30] = {0};
	int *balance;  
	int count=0;
	int tmp;
	
	// 创建共享内存
	mid = get_ipc(sizeof(int));
	if(mid < 0)
	{
		printf("create_ipc error\n");
	}
	balance = (int *)shmat(mid, NULL, 0666);  // 共享内存映射到 balance
	*balance = 0;  // 初始化余额为 100 元
	if (shmdt(balance) == -1)
	{
		// 取消映射
		printf("shmdt() error\n");
	}

	// 创建子进程
	pid = fork();
	if (pid < 0)
	{
		printf("fork() error\n");
		return -1;
	}
	else if (pid ==0)
	{
		strcpy(this, "Child process");
		printf("This is Child process\n");
	}
	else
	{
		strcpy(this, "Parent process");
		printf("This is Parent process\n");
	}
	
	while (count++ < 2000)
	{
		balance = (int *)shmat(mid, NULL, 0);
		tmp = *balance;

		printf("%15s-%4d-get:%d\n", this, count, *balance);
		*balance = tmp + 1;
		printf("%15s-%4d-put:%d\n", this, count, *balance);
		if (shmdt(balance) == -1)
		{
			// 取消映射
			printf("shmdt() error\n");
		}
	}
	
	// 释放共享内存
	if (pid > 0)
	{
		// 父进程等待子进程完成并释放共享内存
		wait(NULL);
		printf("释放共享内存-%d\n", mid);
		if (destory_ipc(mid))
		{
			printf("destory_ipc() error\n");
		}
	}
}
