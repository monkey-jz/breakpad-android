# breakpaddemo

### 简介:

此项目演示了如何把google开源项目breakpad集成进android应用,实现native即C++代码崩溃堆栈信息的获取.在生产环境中app内使用的都是去除符号后的so库,breakpad获取的只是堆栈信息和崩溃指令地址,应用可以把这些信息传至服务器,然后再使用工具分析堆栈信息和崩溃地址定位bug.

#### 获取和编译breakpad

官方文档:[https://github.com/google/breakpad/blob/master/README.md](https://github.com/google/breakpad/blob/master/README.md),
在windows,macOS,linux都可以编译,但复杂程度不一样,为了方便快速我是在linux上编译的.

1.获取depot-tools工具并配置环境

[步骤:](https://commondatastorage.googleapis.com/chrome-infra-docs/flat/depot_tools/docs/html/depot_tools_tutorial.html#_setting_up)

  * 获取depot-tools:

        git clone https://chromium.googlesource.com/chromium/tools/depot_tools.git

  * 配置环境变量:

        export PATH=/path/to/depot_tools:$PATH

2.创建用于放置breakpad源码的文件夹并进入,文件夹名必须为breakpad

    mkdir breakpad && cd breakpad

3.获取breakpad源码并进入源码目录
    
    fetch breakpad  
    cd src
   
4.编译源代码

    ./configure && make
    
   构建完成以后会生成处理so库和堆栈信息的工具,后续会用到这些工具：位于 

    src/processor/minidump_stackwalk, src/processor/minidump_dump
   
   当在linux中编译时还会生成以下工具

    src/tools/linux/dump_syms/dump_syms, src/tools/linux/md2core/minidump-2-core

#### 把breakpad集成进android项目

* 以android library的形式集成braeakpad代码,项目目录结构如下:

      ![](https://i.ibb.co/NZ3qnM2/20200707155143.png)

   官方提供的breakpad android示例工程sample_app是使用.mk文件编译的,此项目是使用CMakelists.txt编译的,[cmake文档](https://cmake.org/cmake/help/latest/manual/cmake.1.html).如图所示在main/cpp目录下新建breakpad目录,把源码中breakpad/src/src目录拷贝到新建的breakpad目录.

* CMkakeLists.txt文件配置 
    
```java

    cmake_minimum_required(VERSION 3.4.1)

    set(BREAKPAD_ROOT ${CMAKE_CURRENT_SOURCE_DIR})

    include_directories(${BREAKPAD_ROOT}/src ${BREAKPAD_ROOT}/src/common ${BREAKPAD_ROOT}/src/common/android/include)

    # List of client source files, directly taken from Makefile.am
    file(GLOB BREAKPAD_SOURCES_COMMON
        ${BREAKPAD_ROOT}/src/client/linux/crash_generation/crash_generation_client.cc
        ${BREAKPAD_ROOT}/src/client/linux/dump_writer_common/thread_info.cc
        ${BREAKPAD_ROOT}/src/client/linux/dump_writer_common/ucontext_reader.cc
        ${BREAKPAD_ROOT}/src/client/linux/handler/exception_handler.cc
        ${BREAKPAD_ROOT}/src/client/linux/handler/minidump_descriptor.cc
        ${BREAKPAD_ROOT}/src/client/linux/log/log.cc
        ${BREAKPAD_ROOT}/src/client/linux/microdump_writer/microdump_writer.cc
        ${BREAKPAD_ROOT}/src/client/linux/minidump_writer/linux_dumper.cc
        ${BREAKPAD_ROOT}/src/client/linux/minidump_writer/linux_ptrace_dumper.cc
        ${BREAKPAD_ROOT}/src/client/linux/minidump_writer/minidump_writer.cc
        ${BREAKPAD_ROOT}/src/client/minidump_file_writer.cc
        ${BREAKPAD_ROOT}/src/common/convert_UTF.c
        ${BREAKPAD_ROOT}/src/common/convert_UTF.cc
        ${BREAKPAD_ROOT}/src/common/md5.cc
        ${BREAKPAD_ROOT}/src/common/string_conversion.cc
        ${BREAKPAD_ROOT}/src/common/linux/elfutils.cc
        ${BREAKPAD_ROOT}/src/common/linux/file_id.cc
        ${BREAKPAD_ROOT}/src/common/linux/guid_creator.cc
        ${BREAKPAD_ROOT}/src/common/linux/linux_libc_support.cc
        ${BREAKPAD_ROOT}/src/common/linux/memory_mapped_file.cc
        ${BREAKPAD_ROOT}/src/common/linux/safe_readlink.cc
        )

    file(GLOB BREAKPAD_S_SOURCE ${BREAKPAD_ROOT}/src/common/linux/breakpad_getcontext.S)

    set_source_files_properties(${BREAKPAD_S_SOURCE} PROPERTIES LANGUAGE C)

    add_library(breakpad STATIC ${BREAKPAD_SOURCES_COMMON} ${BREAKPAD_S_SOURCE})

    target_link_libraries(breakpad log)

```

