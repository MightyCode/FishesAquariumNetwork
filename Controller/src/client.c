#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <errno.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/socket.h>
#include <arpa/inet.h>
#include <unistd.h>
#include <pthread.h>

pthread_t wait_exit;

void * wait_server_exit(void * arg){
    char buffer[256];
    int client_fd = *((int *) arg);
    int n;
    while(1) {
        memset(buffer, 0, sizeof(buffer));
        if ((n = recv(client_fd, buffer, sizeof(buffer), 0)) < 0) {
            perror("Erreur lors de la réception de la réponse du serveur");
            exit(EXIT_FAILURE);
        }

        printf("Message du serveur : %s\n", buffer);

        if (strcmp(buffer, "-1|Bye") == 0 || strcmp(buffer, "-1|Serveur fermé") == 0 || strcmp(buffer, "-1|Timeout") == 0) {
            // Fermeture de la connexion avec le serveur
            close(client_fd);
            exit(EXIT_SUCCESS);
        }
    }
}

int main() {
    int client_fd;
    int portno = 12345;
    struct sockaddr_in serv_addr;
    char buffer[256];
    char buffer_cp[258];
    int n;

    // Création du socket client
    client_fd = socket(AF_INET, SOCK_STREAM, 0);
    if (client_fd < 0) {
        perror("Erreur lors de l'ouverture de la socket");
        exit(EXIT_FAILURE);
    }

    // Configuration de l'adresse du serveur
    memset(&serv_addr, 0, sizeof(serv_addr));
    serv_addr.sin_family = AF_INET;
    serv_addr.sin_addr.s_addr = inet_addr("127.0.0.1"); // Adresse IP du serveur
    serv_addr.sin_port = htons(portno);

    // Connexion au serveur distant
    if (connect(client_fd, (struct sockaddr *)&serv_addr, sizeof(serv_addr)) < 0) {
        perror("Erreur lors de la connexion au serveur");
        exit(EXIT_FAILURE);
    }

    // Réception de la réponse du serveur
    memset(buffer, 0, sizeof(buffer));
    if ((n = recv(client_fd, buffer, sizeof(buffer), 0)) < 0) {
        perror("Erreur lors de la réception de la réponse du serveur");
        exit(EXIT_FAILURE);
    }

    printf("Message du serveur : %s\n", buffer);

    if (strcmp(buffer, "-1|Bye") == 0 || strcmp(buffer, "-1|Serveur fermé") == 0 || strcmp(buffer, "-1|Timeout") == 0) {
        // Fermeture de la connexion avec le serveur
        close(client_fd);
        return 0;
    }

    int val = 1;
    int check_ls = 0;

    pthread_create(&wait_exit, NULL, wait_server_exit, (void *) &client_fd);

    do{ 
        if (fgets(buffer, sizeof(buffer), stdin) != NULL) {
            memset(buffer_cp, 0, strlen(buffer_cp));
            strcpy(buffer_cp, "1|");
            strcat(buffer_cp, buffer);
            val = strcmp(buffer, "log out\n");
            check_ls = 0;
            if (!strcmp(buffer, "ls\n")) {
                check_ls = 1;
            }
            n = write(client_fd, buffer_cp, strlen(buffer_cp));
            if (n < 0) {
                perror("Erreur lors de l'écriture sur la socket");
                exit(EXIT_FAILURE);
            }
            sleep(1);
        } else {
            perror("Erreur lors de la lecture depuis l'entrée standard");
            exit(EXIT_FAILURE);
        }

        if (check_ls == 1) {
            for (int i = 0; i < 10; ++i) {
                // memset(buffer, 0, sizeof(buffer));
                // if ((n = recv(client_fd, buffer, sizeof(buffer), 0)) < 0) {
                //     perror("Erreur lors de la réception de la réponse du serveur");
                //     exit(EXIT_FAILURE);
                // }
                // printf("Message du serveur : %s\n", buffer);
                // if (strcmp(buffer, "-1|") == 0 || strcmp(buffer, "-1|Serveur fermé") == 0 || strcmp(buffer, "-1|Timeout") == 0) {
                //     // Fermeture de la connexion avec le serveur
                //     close(client_fd);
                //     return 0;
                // }
                if (i != 9) {
                    sleep(1);
                }
            }
        }
        // else {
        //     memset(buffer, 0, sizeof(buffer));
        //     if ((n = recv(client_fd, buffer, sizeof(buffer), 0)) < 0) {
        //         perror("Erreur lors de la réception de la réponse du serveur");
        //         exit(EXIT_FAILURE);
        //     }
        //     printf("Message du serveur : %s\n", buffer);
        //     if (strcmp(buffer, "-1|") == 0 || strcmp(buffer, "-1|Serveur fermé") == 0 || strcmp(buffer, "-1|Timeout") == 0) {
        //         // Fermeture de la connexion avec le serveur
        //         close(client_fd);
        //         return 0;
        //     }
        // }
    } while (val != 0);

    // Fermeture de la connexion avec le serveur
    close(client_fd);

    return 0;
}
