cmake_minimum_required(VERSION 3.15)
project(agent)

set(CMAKE_CXX_STANDARD 14)

add_executable(agent main.cpp)


set(THREADS_PREFER_PTHREAD_FLAG ON)
find_package(Threads REQUIRED)
target_link_libraries(agent Threads::Threads)