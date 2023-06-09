#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include <assert.h>
#include <unistd.h>

#include "fish.h"
#include "view.h"
#include "aquarium.h"

int main(){
    struct aquarium * aquarium = malloc(sizeof(struct aquarium));

    aquarium_init(aquarium);
    int size5[2] = {1000,1000};
    aquarium_create(aquarium, size5, "Aquarium");
    
    void (*path)(struct fish *, int, int) = &RandomWayPoint;

    int size1[2] = {10, 5};
    int coords1[2] = {200, 200};

    int size2[2] = {10, 5};
    int coords2[2] = {200, 200};

    int size3[2] = {400,400};
    int coords3[2] = {400,400}; 

    int size4[2] = {400,400};
    int coords4[2] = {400,400}; 
    
    assert(add_fish(aquarium, coords1, size1, "Nathan", path) == 1);
    assert(add_fish(aquarium, coords2, size2, "Victor", path) == 1);
    add_view(aquarium, coords3, size3, "View1");
    add_view(aquarium, coords4, size4, "View2");
    aquarium_print(aquarium);
    assert(del_fish(aquarium, "Nathan") == 1);
    del_view(aquarium, "View2");
    aquarium_print(aquarium);
    update_fishes(aquarium, 5);
    aquarium_print(aquarium);
    start_fish(aquarium, "Victor", 5);
    aquarium_print(aquarium);
    sleep(3);
    update_fishes(aquarium, 5);
    aquarium_print(aquarium);
    sleep(3);
    update_fishes(aquarium, 5);
    aquarium_print(aquarium);
    aquarium_free(aquarium);

    return EXIT_SUCCESS; 
}
