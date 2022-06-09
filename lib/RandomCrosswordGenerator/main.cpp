// This is the main code for the feature that generates the mini crosswords. It is written in C++ insted of Java as it is faster 
// and more reasource effiecent. 
// I included both a Linux and Windows version so you don't need to install a C++ compiler in order to run this app.


#include "word_list.h"
#include <iostream>
#include <time.h>
#include <stdlib.h>
#include <fstream>

std::string crossword[5] = {"     ","     ","     ","     ","     ",}; 
std::string rows_white_spaces[5] ={"","","","",""}; // this is used for keeping track of where the white spaces are in the rows
std::string collumn_white_spaces[5] ={"","","","",""}; // the same above but for collumns
int random_number  = 0;
int r = 0; // r stands for current row
int c = 0; // c stands for current collumn 
int how_many_times = 0; 
int how_many_how_many_times = 0;
int row_to_reset_until = 0;
std::ofstream Outo{"lib/RandomCrosswordGenerator/minicrossword.txt"}; // Linux version
// std::ofstream Outo{"lib/RandomCrosswordGenerator/minicrossword.txt"}; // Windows version

template <typename T>
void writeList(T &List) {  

    int Size = sizeof(List)/sizeof(List[0]);
    for (int i = 0; i < Size; i++) {
       Outo << List[i] << std::endl;
    } 
}
template <typename Y>
bool find_in_list(Y &List, const std::string& string_to_find, int n){ // this function is modified from a BinarySearchString function I found on GeeksforGeeks.com
 int l = 0 ;
        int r = n - 1;
        while (l <= r)
        {
            int m = l + (r - l) / 2;
        int res = -1000;   
        
        if (string_to_find == (List.arr[m].substr(0,string_to_find.length())))
            res = 0;
             
 
            // Check if string to find is present at mid
            if (res == 0)
                return true;
 
            // If string to find greater, ignore left half
            if (string_to_find > (List.arr[m].substr(0,string_to_find.length())))
                l = m + 1;
 
            // If string to find is smaller, ignore right half
            else
                r = m - 1;
        }
 
        return false;
  }

void should_redo_row() { // used as not to get into a situation where there is no available mini crosswords
  how_many_times++;
    if (how_many_times == 50) {
        for (int i = 0; i < 5; i++) { // resets all the non-black space characters. Also excludes the second row
        if (crossword[r+1][i] != '0' && i != 2) {
            crossword[r+1][i] = ' ';
        }
    }
        r -= 1;
        row_to_reset_until = r-1;
        how_many_times = 0;
        how_many_how_many_times++;
           if (how_many_how_many_times == 50) {
               for (int n = r; n >= row_to_reset_until; n--) { // resets all the non-black space characters. Also excludes the second row
                for (int i = 0; i < 5; i++) {
                    if (crossword[r+1][i] != '0' && i != 2) {
                        crossword[r+1][i] = ' ';
                            }
                        }
                        r -= 1; }
                     how_many_how_many_times = 0;
           }
    }
}

void make_collumn_string(std::string& output){ // filters out all non-black and space characters
    for (int i = 0; i < 5; i++) {
        if (crossword[i][c] != '0' && crossword[i][c] != ' ') {
            output.push_back(crossword[i][c]);
        }
    }
}

void make_third_collumn() {
     int Size = sizeof(Five_Letter_Words.arr)/sizeof(Five_Letter_Words.arr[0]);
    random_number = rand()% Size + 0;
    for (int i = 0; i < 5; i++) {
        crossword[i][2] = Five_Letter_Words.arr[random_number][i];
    }
}

template <typename Z>
int make_row(const Z& Word_List ) {
random_number = 0;   
int Size = sizeof(Word_List.arr)/sizeof(Word_List.arr[0]);
random_number = rand() % Size + 0;
    if (Word_List.arr[random_number]['2'-rows_white_spaces[r][0]] == crossword[r][2]) { 
       crossword[r].replace(rows_white_spaces[r][0]-'0',Word_List.arr[random_number].length(),Word_List.arr[random_number]);
        std::string collumn_string {""};
            for (c = 0; c < 5; c++) {
                collumn_string.clear();
                make_collumn_string(collumn_string);
                switch(collumn_white_spaces[c][1]-collumn_white_spaces[c][0]+1) {
                    case 3: 
                        if(find_in_list(Three_Letter_Words,collumn_string,sizeof(Three_Letter_Words.arr)/sizeof(Three_Letter_Words.arr[0])) == false) {
                            r -= 1;
                            should_redo_row();
                            return 0;
                        }  break;
                    case 4: 
                        if(find_in_list(Four_Letter_Words,collumn_string,sizeof(Four_Letter_Words.arr)/sizeof(Four_Letter_Words.arr[0])) == false) {
                            r -= 1;
                            should_redo_row();
                            return 0;
                        }  break;    
                    case 5: 
                        if(find_in_list(Five_Letter_Words,collumn_string,sizeof(Five_Letter_Words.arr)/sizeof(Five_Letter_Words.arr[0])) == false) {
                            r -= 1;
                            should_redo_row();
                            return 0;
                        }  break;      
               
                }

            }
         how_many_times = 0;
    } else {
      r -= 1;
    }
    return 1;
}

void make_random_black_squares() {
    random_number = rand() % 2 + 1;
    if (random_number == 1) {
        crossword[0][0] = '0';
        random_number = rand() % 2 + 1;
            if (random_number == 1) {
                random_number = rand() % 2 + 1;
                if (random_number == 1){
                    crossword[0][1] = '0';
                } else {
                    crossword[1][0] = '0';
                }

            }
    }
    random_number = rand() % 2 + 1;
    if (random_number == 1) {
        crossword[4][0] = '0';
        random_number = rand() % 2 + 1;
            if (random_number == 1) {
                random_number = rand() % 2 + 1;
                if (random_number == 1){
                    crossword[4][1] = '0';
                } else {
                    if (crossword[0][0] != '0') {
                    crossword[3][0] = '0'; }
                }

            }
    }
    random_number = rand() % 2 + 1;
    if (random_number == 1 && crossword[0][1] != '0') {
        crossword[0][4] = '0';
        random_number = rand() % 2 + 1;
            if (random_number == 1) {
                random_number = rand() % 2 + 1;
                if (random_number == 1){
                    crossword[1][4] = '0';
                } else {
                    if (crossword[0][0] != '0') {
                    crossword[0][3] = '0'; }
                }

            }
    }
    random_number = rand() % 2 + 1;
    if (random_number == 1 && crossword[4][1] != '0' && crossword[1][4] != '0') {
        crossword[4][4] = '0';
        random_number = rand() % 2 + 1;
            if (random_number == 1) {
                random_number = rand() % 2 + 1;
                if (random_number == 1){
                    if (crossword[4][0] != '0') {
                    crossword[4][3] = '0'; }
                } else {
                    if (crossword[0][4] != '0') {
                    crossword[3][4] = '0'; }
                }

            }
    }
    for (int a = 0; a < 5; a++) {
        bool which_side = true;
        for (int b = 0; b < 5; b++) {
            if(crossword[a][b] == ' ' && which_side) {
                rows_white_spaces[a] += std::to_string(b);
                which_side = false;
            }
            if(crossword[a][b] == '0' && !which_side) {
                which_side = true;
                rows_white_spaces[a] += std::to_string(b-1);
            }
            if (!which_side && b == 4){
                rows_white_spaces[a].push_back('4'); 
            }
        }
    }
    for (int a = 0; a < 5; a++) {
        bool which_side = true;
        for (int b = 0; b < 5; b++) {
            if(crossword[b][a] == ' ' && which_side) {
                collumn_white_spaces[a] += std::to_string(b);
                which_side = false;
            }
            if(crossword[b][a] == '0' && !which_side) {
                which_side = true;
                collumn_white_spaces[a] += std::to_string(b-1);
            }
            if (!which_side && b == 4){
                collumn_white_spaces[a].push_back('4'); 
            }
        }
    }

}



int main() {
    srand(time(NULL)); // sets the random number generator to output number every time
    make_random_black_squares(); 
    make_third_collumn();  // I chose to make the third collumn prematurely becuase it helps the computers rule out non-possible combinations quicker
   for ( r = 0; r < 5; r++) {
        switch(rows_white_spaces[r][1]-rows_white_spaces[r][0]+1) { 
           case 3 : make_row(Three_Letter_Words); break;
           case 4 : make_row(Four_Letter_Words); break;
           case 5 : make_row(Five_Letter_Words); break;
        }
    } 
    writeList(crossword); 

    Outo.close();
    return 0;
}