int create_ipc(int);
int get_ipc(int);
int destory_ipc(int);

int get_sem(int num);
int set_sem_val(int id, int index, int val);
int del_sem(int id, int index);
int semwait(int id, int index);
int sempost(int id, int index);

