
cmake_minimum_required(VERSION 3.4.1)


include_directories(src/main/cpp/include)


add_library(
            compress
            SHARED
        src/main/cpp/compress/compress.c

            )



add_library( libjpeg SHARED IMPORTED)
set_target_properties( libjpeg
        PROPERTIES IMPORTED_LOCATION
        ${CMAKE_SOURCE_DIR}/libs/${CMAKE_ANDROID_ARCH_ABI}/libjpeg.so)



find_library( log-lib log)
find_library(graphics jnigraphics)


target_link_libraries(
                compress
                libjpeg
                ${log-lib}
                ${graphics}
                )

