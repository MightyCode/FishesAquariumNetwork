#include "view.h"

int main(int argc, char** argv){
    struct view * view = malloc(sizeof(struct view));

    view_init(view);

    int size[2] = {400,400};
    int coords[2] = {400,400}; 

    view_create(view, size, coords, argv[1]);

    return 0;
}