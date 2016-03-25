#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <assert.h>
#include <time.h>
#include "bstrlib.h"

/*
* Rock, Paper Scissors, Lizard, Spock game
* https://www.reddit.com/r/dailyprogrammer/comments/23lfrf/4212014_challenge_159_easy_rock_paper_scissors/
*/

typedef struct Game {
    char *player1;
    char *action;
    char *player2;
} Game;

#define MOVES 10
#define PICK 5 /* What computer selects */

/* Number of moves 10 */
Game *games[MOVES];


Game *Game_create(char *player1, char *action, char *player2)
{
    Game *game = calloc(1, sizeof(Game));
    assert(game != NULL);

    game->player1 = strdup(player1);
    game->action = strdup(action);
    game->player2 = strdup(player2);

    return game;
}

int result(int human, int computer);
int draws = 0, hwins = 0, cwins = 0;
char *choices[6]={"Null", "Scissors", "Paper", "Rock", "Lizard", "Spock"};

int main()
{

    /* Random seed */
    unsigned int seed = (unsigned int)time(NULL);
    srand(seed);

    /* Moves */
    games[0] = Game_create("Scissors", "cut", "paper");
    games[1] = Game_create("Paper", "covers", "rock");
    games[2] = Game_create("Rock", "crushes", "lizard");
    games[3] = Game_create("Lizard", "poisons", "Spock");
    games[4] = Game_create("Spock", "smashes", "scissors");
    games[5] = Game_create("Scissors", "decapitate", "lizard");
    games[6] = Game_create("Lizard", "eats", "paper");
    games[7] = Game_create("Paper", "disproves", "Spock");
    games[8] = Game_create("Spock", "vaporizes", "rock");
    games[9] = Game_create("Rock", "crushes", "Scissors");

    /* Move computer or human pick up */
    int computer = 0;
    int human = 0;

    /* Initial */
    printf("Rock, paper, scissors, lizard, spock game\n");


    while(1) {
        printf("\n========\n");
        printf("Pick a choice\n");
        printf("1> Scissors\n");
        printf("2> Paper\n");
        printf("3> Rock\n");
        printf("4> Lizard\n");
        printf("5> Spock\n");
        printf("q> Quit!\n");
        printf("==========\n");

        printf("Human: ");

        char line[256];
        if(fgets(line, sizeof(line), stdin) != NULL) {

                    /* We are more civilized than this, thus will use bstrlib for comparision later. */  
            if(strcmp(line, "q\n") == 0 || strcmp(line, "Q\n") == 0) {
                printf("Thanks for playing\n");

                printf("\nResult: Wins: Human: %d, Computer: %d :: Draws: %d\n\n", hwins, cwins, draws);

                exit(0);
            }

            human = atoi(line);
            if (human < 1 || human > 5) {
                printf("Input %d out of range\n", human);                   
                continue;
            }
        }

        computer = rand() % PICK + 1;

        printf("\n");
        printf("Player picks: %s\n", choices[human]);
        printf("Computer picks: %s\n", choices[computer]);

        /* If both select same */
        if (human == computer) {
            draws++;
            printf("Draw !!\n");
            continue;
        }

        int output = result(human, computer);
        printf("%s %s %s.\n", games[output]->player1, games[output]->action, games[output]->player2);

    }   

    return 0;

}



int result(int human, int computer)
{
    bstring humanchoice = bfromcstr(choices[human]);
    bstring computerchoice = bfromcstr(choices[computer]);

    int i;
    for(i = 0; i < MOVES; i++) {
        if(bstricmp(bfromcstr(games[i]->player1), humanchoice) == 0 && bstricmp(bfromcstr(games[i]->player2), computerchoice) == 0) {
            hwins++;
            printf("Human Wins !!\n");
            return i;
        }
        else if(bstricmp(bfromcstr(games[i]->player1), computerchoice) == 0 && bstricmp(bfromcstr(games[i]->player2), humanchoice) == 0) {
            cwins++;
            printf("Computer Wins !!\n");
            return i;
        }

    }

    return 0;
}
