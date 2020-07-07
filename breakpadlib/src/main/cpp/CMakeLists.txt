cmake_minimum_required(VERSION 3.4.1)

set(ENABLE_INPROCESS ON)
set(ENABLE_OUTOFPROCESS ON)
set(ENABLE_LIBCORKSCREW ON)
set(ENABLE_LIBUNWIND ON)
set(ENABLE_LIBUNWINDSTACK ON)
set(ENABLE_CXXABI ON)
set(ENABLE_STACKSCAN ON)
if (${ENABLE_INPROCESS})
    add_definitions(-DENABLE_INPROCESS)
endif ()
if (${ENABLE_OUTOFPROCESS})
    add_definitions(-DENABLE_OUTOFPROCESS)
endif ()

set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -Werror=implicit-function-declaration")
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11 ")

include_directories(breakpad/src breakpad/src/common/android/include)
add_subdirectory(breakpad)
list(APPEND LINK_LIBRARIES breakpad)
add_library(breakpad_core SHARED
       breakpadlib.cpp)
target_link_libraries(breakpad_core
                      ${LINK_LIBRARIES}
                      log)