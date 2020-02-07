CPP_FLAGS = -ggdb -Wall -Werror -std=c++11 -O3

NAMES = agent

all: $(NAMES).o
	g++ $(NAMES).o -lpthread $(CPP_FLAGS) -o agent

$(NAMES).o: $(NAMES).cpp
	g++ $(NAMES).cpp $(CPP_FLAGS) -c

clean:
	rm -f agent *.o *~
