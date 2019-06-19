
当列表中含有计时器的情况下，如果滑动列表，由于复用会引起计时器计时混乱，如果时间是从服务器取值，item复用的时候计时器会重新计时。

1、滑动错乱问题

如下图首先将timer对象存到holder中，然后在adapter中重写onViewRecycled方法，然后将timer取出并调用timer的cancel方法。

[![VO4Ksg.png](https://s2.ax1x.com/2019/06/19/VO4Ksg.png)](https://imgchr.com/i/VO4Ksg)

2、从服务器获取计时时间时，滑动列表出现重新计时的问题
设置一个全局变量isCount只有刷新列表数据的时候才获取计时时间