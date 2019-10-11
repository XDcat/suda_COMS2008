#include<unistd.h>
#include<stdlib.h>
#include<stdio.h>
#include<sys/types.h>
int main()
{
  pid_t  pid;
        /* 创建进程  */
        pid = fork();
        if (pid < 0) { /* 出现错误  */
                fprintf(stderr, "Fork Failed");
                exit(-1);
        }
        else if (pid == 0) { /* 子进程  */
                printf("This is child process \n");
        }
        else { /* 父进程 */
                /* 等待子进程完成 */
                printf("This is parent process \n");
                printf("Wait child process \n");
                wait (NULL);
                printf ("Child Complete \n");
                exit(0);
        }
}
