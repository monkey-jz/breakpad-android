# breakpaddemo

###简介:
 native crash demo

![](https://i.ibb.co/wYWDbQF/20200707114418.png)

此demo演示了如何把google开源项目breakpad集成进android应用,实现native即C++代码崩溃堆栈信息的获取.在生产环境中app内使用的都是去除符号后的so库,breakpad获取的只是堆栈信息和崩溃指令地址,应用可以把这些信息传至服务器,然后再使用工具分析堆栈信息和崩溃地址定位bug.

####1.获取和编译breakpad

官方文档:[https://github.com/google/breakpad/blob/master/README.md](https://github.com/google/breakpad/blob/master/README.md),
在windows,macOS,linux都可以编译,但复杂程度不一样,为了方便快速我是在linux上编译的.
#####1.1获取depot-tools工具并配置环境

步骤:[https://commondatastorage.googleapis.com/chrome-infra-docs/flat/depot_tools/docs/html/depot_tools_tutorial.html#_setting_up](https://commondatastorage.googleapis.com/chrome-infra-docs/flat/depot_tools/docs/html/depot_tools_tutorial.html#_setting_up )


获取depot-tools:

    git clone https://chromium.googlesource.com/chromium/tools/depot_tools.git

配置环境变量:

    export PATH=/path/to/depot_tools:$PATH
#####1.2创建用于放置breakpad源码的文件夹并进入,文件夹名必须为breakpad
    mkdir breakpad && cd breakpad
#####1.3获取breakpad源码并进入源码目录

    fetch breakpad  

    cd src

#####1.4编译源代码
    ./configure && make
    
   构建完成以后会生成处理so库和堆栈信息的工具,后续会用到这些工具：位于 

    src/processor/minidump_stackwalk, src/processor/minidump_dump
   
   当在linux中编译时还会生成以下工具

    src/tools/linux/dump_syms/dump_syms, src/tools/linux/md2core/minidump-2-core

####2.把breakpad集成进android项目

#####2.1以android library的形式集成braeakpad代码,项目目录结构如下:

![](https://i.ibb.co/NZ3qnM2/20200707155143.png)


