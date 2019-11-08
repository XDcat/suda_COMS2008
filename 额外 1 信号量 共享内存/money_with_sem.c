#include <stdio.h>
#include <semaphore.h>
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
	int *balance;  // 银行账户, 余额为 0 元
	int count=0;
	int tmp;
	
	// 初始化信号量, 并存入共享内存中
	int sems_id;

	sems_id = get_sem(2);
	set_sem_val(sems_id, 0, 0);  // 用于同步
	set_sem_val(sems_id, 1, 1);  // 用于互斥
				
	// 创建共享内存
	mid = get_ipc(sizeof(int));
	if(mid < 0)
	{
		printf("create_ipc error\n");
	}
	balance = (int *)shmat(mid, NULL, 0);  // 共享内存映射到 balance
	*balance = 0;  // 初始化余额为 0 元
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
		// 子进程
		printf("This is Child process\n");
		// 父子进程同步
		printf("通知 Parent process\n");
		sempost(sems_id, 0);

		strcpy(this, "Child process");
	}
	else
	{
		// 父进程
		printf("This is Parent process\n");
		// 父子进程同步
		printf("等待 Child process\n");
		semwait(sems_id, 0);

		strcpy(this, "Parent process");
	}

	// 对临界区的操作
	while (count++ < 2000)
	{
		semwait(sems_id, 1);
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
		sempost(sems_id, 1);
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
		printf("释放信号量集-%d\n", sems_id);
		if (del_sem(sems_id, 0) < 0)
			printf("del_sem() eooro\n");
	}
}

