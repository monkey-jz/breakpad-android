cmake_minimum_required(VERSION 3.4.1)

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