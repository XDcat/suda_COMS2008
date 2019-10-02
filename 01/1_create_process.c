#include<unistd.h>
#include<stdlib.h>
#include<stdio.h>
#include<sys/types.h>
int main()
{
  pid_t  pid;
	/* fork another process */
	pid = fork();
	if (pid < 0) { /* error occurred */
		fprintf(stderr, "Fork Failed");
		exit(-1);
	}
	else if (pid == 0) { /* child process */
		printf("This is child process \n");
		execlp("/bin/ls", "ls", NULL);
	}
	else { /* parent process */
		/* parent will wait for the child to complete */
		printf("This is parent process \n");
		wait (NULL);
		printf ("Child Complete \n");
		exit(0);
	}
}
