### 1、git commit --amend 
&emsp;发现-m的说明文字写的有问题，想要重新写一次，也就是想撤销上次的提交动作，重新提交一次
### 2、git branch -r
&emsp;查看远程分支
### 3、pull操作
- git pull origin &lt;origin&gt;:&lt;local&gt;&emsp;将远程指定分支拉取到本地指定分支；
- git pull origin &lt;origin&gt;&emsp;将远程指定分支拉取到本地当前分支；
- git pull origin &emsp;将与本地当前分支同名远程分支拉取到本地当前分支；
### 4、push操作
- git push origin &lt;local&gt;:&lt;orign&gt;&emsp;将本地指定分支推到远程指定分支；
- git push origin &lt;local&gt;&emsp;将本地当前分支推到同名的远程分支；
- git push origin &emsp;前分支与远程分支存在追踪关系，将本地当前分支推到同名的远程分支；
### 5、git oush --set -upstream origin &lt;本地分支名&gt;
&emsp;将本地分支与远程同名分支相关联
### 6、撤销操作
- git revert HEAD &emsp;撤销最近一个提交
- git reset --mixed &emsp;取消commit+add
- git reset --soft &emsp;取消commit
- git reset --hard &emsp;取消commit+add+local working