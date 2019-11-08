#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/types.h>
#include <sys/shm.h>
#include <sys/sem.h>
#include <sys/ipc.h>

#include "common.h"

union semun
{
	    int val;
		    struct semid_ds *buf;
			    unsigned short *arry;
};

int share_memory(int ipc_size, int flag)
{
	int id;
	// 获取 key_t
	key_t key = ftok("/tmp", 66);
	if (key < 0)
	{
		printf("ftok() error\n");
		return -1;
	}

	// 创建 共享内存
	id = shmget(key, ipc_size, flag);
	if (id < 0)
	{
		printf("shmget() error\n");
		return -1;
	}

	return id;
}

int create_ipc(int ipc_size)
{
	// 创建共享内存，如果已经存在则报错
	// return 共享内存 id
	return share_memory(ipc_size, IPC_CREAT|IPC_EXCL|0666);
}

int get_ipc(int ipc_size) 
{
	// 获取共享内存，如果不存在则创建
	// return 共享内存 id
	return share_memory(ipc_size, IPC_CREAT|0666);
}

int destory_ipc(int id)
{
	// 销毁共享内存
	// return 如果失败返回-1
	return shmctl(id, IPC_RMID, NULL);
}


int get_sem(int num)
{
	int id = semget((key_t) 1234, num, IPC_CREAT|0666);
	if (id < 0)
	{
		printf("semget() error\n");
		return -1;
	}
	return id;
}

int set_sem_val(int id, int index, int val)
{
	union semun sem_union;
	sem_union.val = val;
	if (semctl(id, index, SETVAL, sem_union) < 0)
	{
		printf("semctl() set error\n");
		return -1;
	}

	return 1;
}

int del_sem(int id, int index)
{
	union semun sem_union;
	if (semctl(id, index, IPC_RMID, sem_union) < 0)
	{
		printf("semctl() del error\n");
		return -1;
	}
	return 1;

}

int semwait(int id, int index)
{
	struct sembuf sem_b;
	sem_b.sem_num = index;
	sem_b.sem_op = -1;
	sem_b.sem_flg = SEM_UNDO;
	if (semop(id, &sem_b, 1) < 0)
	{
		printf("semop() wait error\n");
		return -1;
	}
	return 1;
}


int sempost(int id, int index)
{
	struct sembuf sem_b;
	sem_b.sem_num = index;
	sem_b.sem_op = 1;
	sem_b.sem_flg = SEM_UNDO;
	if (semop(id, &sem_b, 1) < 0)
	{
		printf("semop() post error\n");
		return -1;
	}
	return 1;
}
