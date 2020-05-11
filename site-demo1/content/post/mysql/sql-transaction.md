+++
title = "MySQL事务"
date = "2019-08-21"
tags = ["Sql","事务"]
categories = ["MySQL"]
+++
# MySQL事务
数据库事务主要用于处理操作量大，复杂度高的数据。比如说在银行系统中，A转账给B，A的钱减少，B的钱增多，A、B的成功和失败应该是一致的，这样一个数据库操作语句就构成一个事务。

- Innodb数据库引擎才支持事务
- 事务用来维护数据的完整性，保证成批的SQL语句要么全部执行，要么全部不执行
- 事务用来管理insert、update、delete
## 一、MySQL事务介绍
一般来说事务必须满足4个条件（ACID）：`原子性`，`一致性`，`隔离性`，`持久性`

- 原子性（Atomicity）：一个事务中的操作，要么全部完成，要么全部不完成，不会停留在某一个阶段。执行过程中失败或发生错误，会回滚到事务开始之前的状态。
- 一致性（Consistency）：在事务开始之前和结束之后，数据库的完整性没有被破坏。这表示写入的数据完全符合所有规则。
- 隔离性（Isolation）：数据库允许多个并发事务对数据库进行读写和修改，隔离性防止多个事务并发执行时由于交叉执行导致的数据库数据不一致。事务隔离分为不同的级别，分别为：读未提交、读已提交、可重复读和串行化
- 持久性：事务处理结束后，对数据的修改是永久性的。即使系统故障也不会丢失
>在MySQL的默认设置下事务都是自动提交的，即执行SQL后将马上执行COMMIT操作。即要显示的开启一个事务，需要关闭事务的自动提交，jdbc中可设置为`connect.setAutoCommit(false)`，执行成功则调用`connect.commit()`，执行失败则调用`connect.rollback()`进行事务回滚

## 二、事务使用的问题
由于数据库的隔离级别分为多个级别，在并发访问时将出现一些问题：

1. 脏读：B事务读取到了A事务未提交的数据—————->(要求B事务读取A事务已经提交的数据)

2. 不重复读：一个事务中 两次读取的数据内容不一致—>(要求一个事务多次读取数据时一致的)update

3. 幻读/虚读：一个事务中 两次读取的数据数量不一致–>(要求一个事务多次读取的数据数量一致)insert、delete

不同的隔离级别可以解决不同的问题：

1. 读未提交（read uncommited）：读取尚未提交的数据–>最低隔离级别，上述1、2、3中情况都可能发生
2. 读已提交（read commited）：读取已经提交的数据——>可以解决脏读 —- oracle默认的
3. 可重复读（repeatable read）:可重复读取——————–>可以解决脏读 和 不可重复读 —mysql默认的
4. 串行化（serializable）：串行化———————————->可以解决 脏读 不可重复读 和 虚读—相当于锁表