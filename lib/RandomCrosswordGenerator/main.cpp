// This is the main code for the feature that generates the mini crosswords. It is written in C++ insted of Java as it is faster 
// and more reasource effiecent. 
// I included both a Linux and Windows version so you don't need to install a C++ compiler in order to run this app.


#include "word_list.h"
#include <iostream>
#include <time.h>
#include <stdlib.h>
#include <algorithm>
#include <fstream>

std::string Os  = "Linux"; // change this to "Windows" if you are on Windows
std::string crossword[5] = {"     ","     ","     ","     ","     ",};
std::string input_crossword[5] = {"     ","     ","     ","     ","     ",};
std::string rows_white_spaces[5] ={"","","","",""}; // this is used for keeping track of where the white spaces are in the rows
std::string collumn_white_spaces[5] ={"","","","",""}; // the same above but for collumns
int random_number  = 0;
int how_many_times_didnt_fit = 0;
int r = 0; // r stands for current row
int c = 0; // c stands for current collumn 
bool useInput = true;
bool onlyUserWords = false;
int how_many_times = 0; 
int how_many_how_many_times = 0;
int how_many_how_many_how_many_times = 0;
int row_to_reset_until = 0;
std::ofstream Outo; 
std::ifstream Into; 
std::vector<std::string> user_crossword_clues;
std::string relativePath;




template <typename T>
void writeList(T &List) {  
    int Size = sizeof(List)/sizeof(List[0]);
    for (int i = 0; i < Size; i++) {
       Outo << List[i] << std::endl;
    } 

}



void add_user_words(){
    if (Os == "Linux") {
        Into.open(relativePath+"/otherWords.txt");
    } else {
        Into.open(relativePath+"\\otherWords.txt");
    }
    std::string words{""};
    while (std::getline(Into,words)) {
        if (words != "") {
            Words_Lists[0].push_back(words.substr(0,words.find('@')));
            user_crossword_clues.push_back(words.substr(words.find('@')+1));
        }
    }
    for (int i = 0; i < Words_Lists[0].size(); i++) {
        words = Words_Lists[0][i];
        int index = find_in_list(Words_Lists[words.length()-2], words);
        if (index == -1) {
         Words_Lists[words.length()-2].insert(std::upper_bound(Words_Lists[words.length()-2].begin(),Words_Lists[words.length()-2].end(),words),words);
        } else {
            Words_Lists[words.length()-2][index] = words;
        }
    }

    for (int a = 0; a < 4; a++) {
        char startingCharacter = 'A';
        bool found = false;
        while (  Words_Lists[a][0][0]-startingCharacter != 0) {
                    Words_Lists_Begginings_And_Ends[a].push_back(-1);
                    Words_Lists_Begginings_And_Ends[a].push_back(-1);
                    startingCharacter++;
        }
        Words_Lists_Begginings_And_Ends[a].push_back(0);
        for (int i = 0; i < Words_Lists[a].size(); i++) {
            if (Words_Lists[a][i][0] != startingCharacter) {
                Words_Lists_Begginings_And_Ends[a].push_back(i-1);
                while (  Words_Lists[a][i][0]-startingCharacter != 1) {
                    Words_Lists_Begginings_And_Ends[a].push_back(-1);
                    Words_Lists_Begginings_And_Ends[a].push_back(-1);
                    startingCharacter++;
                }
                startingCharacter++;
                Words_Lists_Begginings_And_Ends[a].push_back(i);

            }
        }
        Words_Lists_Begginings_And_Ends[a].push_back(Words_Lists[a].size()-1);
    }


    Into.close();
}  

void make_input_crossword(){
    if (Os == "Linux") {
        Into.open(relativePath+"/input.txt");
    } else {
        Into.open(relativePath+"\\input.txt");
    }
    Into >> useInput;
    Into >> onlyUserWords;

    std::string string{""};
    std::getline(Into,string); // for some reason the next std::getline always returns "" so I added this
    for (int i = 0; i < 5; i++) {
        std::getline(Into,string);
        if (string == "" ) {
            string = "     ";
        }
        input_crossword[i] = string;
        crossword[i] = string;
    } 
    Into.close();
}

void make_collumn_string(std::string& output){ // filters out all non-black and space characters
    int spaceInTheMiddle = 0;
    for (int i = 0; i < 5; i++) {
        if ( crossword[i][c] ==' ' && crossword[i+1][c]  ) {
            spaceInTheMiddle++;
            
        }
        else if (crossword[i][c] != '0') {
            for (int i = 0; i < spaceInTheMiddle; i++) {
                output.push_back(' ');
            }
            spaceInTheMiddle = 0;
            output.push_back(crossword[i][c]);
        }
    }


}

void make_third_collumn() {
    bool cont = false;
    int findInListPosition = 0;

    std::string otherWord{""};
    std::string word{""};
    std::string rowWord{""};
    std::string rowString{""};
     int Size = Words_Lists[3].size();
    if (onlyUserWords) {
        Size = Words_Lists[0].size();
    }

    for (int i = 0; i < 5; i++) {
       otherWord.push_back(input_crossword[i][2]);
    }

    if (otherWord.find(' ') != -1) {

     do {
    word.clear();
    random_number = rand()% Size + 0;
    cont = true;
    if (!onlyUserWords) {
    word = Words_Lists[3][random_number];
    } else {
        while (word.length() != 5) {
                random_number = rand()% Size + 0;
            word = Words_Lists[0][random_number];   
        }
    }



    for (int i = 0; i < 5; i++) {
        if ((input_crossword[i][2] != word[i] && input_crossword[i][2] != ' ')  ) {
            cont = false;
            break;
        }
    }

   if (cont) {
        for (int i = 0; i < 5; i++) {

        if (crossword[i].find(' ') != -1 && !useInput ) {        
                rowString = crossword[i];
                rowString[2] = word[i];
                rowWord.clear();
            for (int a = rows_white_spaces[i][0]-'0'; a < rows_white_spaces[i][1]-'0'+1; a++) {
                rowWord.push_back(rowString[a]);
            }
                if (!onlyUserWords) {
                    findInListPosition = rowWord.size()-2;
                }

            for (int a = 0; a < Words_Lists[findInListPosition].size();  a++ ) {
                cont = true;
                    if (Words_Lists[findInListPosition][a].length() == rowWord.length()) {
                        for (int b = 0; b < rowWord.length(); b++) {
                            if (rowWord[b] != ' ' && Words_Lists[findInListPosition][a][b] != rowWord[b]) {
                                cont = false;
                                break;
                            }
                        }
                    } else {
                        cont = false;
                    }
                    if (cont) {
                        break;
                    }

            }
            if (!cont) {
                    break;
            }
        } 

        }
    } 

     } while (!cont); 
     
     } else {
        word = otherWord;
     }
     

    for (int i = 0; i < 5; i++) {
        crossword[i][2] = word[i];
    }
     
}

void should_redo_row() { // used as not to get into a situation where there is no available mini crosswords
  how_many_times++;
    if (how_many_times == 25 && r >= -0) {

        for (int i = 0; i < 5; i++) { // resets all the non-black space characters. Also excludes the second row
        if (crossword[r+1][i] != '0' && i != 2 && input_crossword[r+1][i] == ' ') {
            crossword[r+1][i] = ' ';
        }
    }
        r -= 1;
        row_to_reset_until = r-1;
        how_many_times = 0;
        how_many_how_many_times++;
           if (how_many_how_many_times == 25) {
                how_many_how_many_how_many_times++;
               for (int n = r; n >= row_to_reset_until; n--) { // resets all the non-black space characters. Also excludes the second row
                for (int i = 0; i < 5; i++) {
                    if (crossword[r+1][i] != '0' && i != 2 && input_crossword[r+1][i] == ' ') {
                        crossword[r+1][i] = ' ';
                            }
                        }
                        if (r >= 0) {
                        r -= 1;
                        }
                         }
                     how_many_how_many_times = 0;

                    if (how_many_how_many_how_many_times == 5) {
                        how_many_how_many_how_many_times = 0;
                        make_input_crossword();
                        make_third_collumn();
                        r = -1;
                    } 
           }
    }
}



inline bool wordsFit(const std::string& word, const std::string& otherWord) {
    for (int i = rows_white_spaces[r][0]-'0'; i < rows_white_spaces[r][1]-'0'+1; i++) {
        if (word[i-rows_white_spaces[r][0]+'0'] != otherWord[i] && otherWord[i] != ' ') {
            return false;
        }
    }
    return true;
}

int make_row(const std::vector<std::string>& Word_List ) {
    int findInListPosition = 0;
    random_number = 0;   
    int Size = Word_List.size();
    random_number = rand() % Size + 0;
    std::string wordListWord = Word_List[random_number];
        while (wordListWord.length() != rows_white_spaces[r][1]-rows_white_spaces[r][0]+1) {
            random_number = rand() % Size + 0;
            wordListWord = Word_List[random_number];
        }

        if (  ( ( wordListWord['2'-rows_white_spaces[r][0]] == crossword[r][2]) && (!useInput || wordsFit(wordListWord,input_crossword[r])) )  || ( input_crossword[r].find(' ') == -1 )  ) { 
            if (input_crossword[r].find(' ') != -1) {
        crossword[r].replace(rows_white_spaces[r][0]-'0',wordListWord.length(),wordListWord);
        for (int i = 0; i < 5; i++) { // for some reason there is glitch when useInput is false that makes the '0' turn into space, I'll try to fix it later
            if (crossword[r][i] == ' ') {
                crossword[r][i] = '0';
            }
        }

            }
            std::string collumn_string {""};
                for (c = 0; c < 5; c++) {
                    collumn_string.clear();
                    make_collumn_string(collumn_string);
                    bool wordIsFull = true;
                    if (useInput ) {
                        for (int i = 0; i < 5; i++) {
                            if ( input_crossword[i][c] == ' ') {
                                wordIsFull = false;
                                break;
                            }
                        }
                    }
                    if ( (!wordIsFull && crossword[c][r] != '0')  || !useInput ) {
                                if (!onlyUserWords) {
                                    findInListPosition = collumn_white_spaces[c][1]-collumn_white_spaces[c][0]-1;
                                }
                                if (useInput && collumn_string.find(' ') != -1) {
                                   bool cont = true;
                                   std::string otherWord{""};
                                   int startIndex = 0 ;
                                   int endIndex = Words_Lists[findInListPosition].size()-1;
                                    if (collumn_string[0] != ' ') {
                                        startIndex = Words_Lists_Begginings_And_Ends[findInListPosition][ (collumn_string[0]-'A')*2 ];
                                        endIndex = Words_Lists_Begginings_And_Ends[findInListPosition][ (collumn_string[0]-'A')*2+1 ];
                                    }
                                    for (int i = startIndex; i < endIndex+1; i++) {
                                       cont = true;
                                        otherWord = Words_Lists[findInListPosition][i];
                                        
                                        for (int a = 0; a < collumn_string.length(); a++) {
                                            if (collumn_string[a] != ' ' && otherWord[a] != collumn_string[a]) {
                                                cont = false;
                                                break;
                                            }
                                        }
                                        if (cont) {
                                            break;
                                        }
                                    }
                                    if (!cont) {
                                        r -= 1;
                                        should_redo_row();
                                        return 0;
                                    }
                                } 
                                else if(collumn_string != "" && find_in_list( Words_Lists[findInListPosition] ,collumn_string, Words_Lists_Begginings_And_Ends[findInListPosition][ (collumn_string[0]-'A')*2 ],Words_Lists_Begginings_And_Ends[findInListPosition][ (collumn_string[0]-'A')*2 +1])  == -1) {
                                r -= 1;
                                    should_redo_row();
                                    return 0;
                                }  
                    }
                }
            how_many_times = 0;
        } else {
            how_many_times_didnt_fit++;
        r -= 1;
        }
        return 1;
}

void make_random_black_squares() {
    for (int i = 0; i < 5; i++) {
        rows_white_spaces[i].clear();
        collumn_white_spaces[i].clear();
    }

   if (!useInput) {

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
    if (random_number == 1 && crossword[1][0] != '0') {
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
                } 
                random_number = rand() % 2 +1;
                if (random_number == 1){
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
                } 
                random_number = rand() % 2 + 1;
                if (random_number == 1) {
                    if (crossword[0][4] != '0') {
                    crossword[3][4] = '0'; }
                    
                }

            }
    } 
     
}


     int position = 0;
      std::string word{""};
    for (int a = 0; a < 5; a++) {
          position = 0;
        while ( crossword[a].find('0',position) == position) {
            position++;
        }


        rows_white_spaces[a] += std::to_string(position);
        position = crossword[a].find('0',position);
        if (position == -1) {
            position = 5;
        }

        rows_white_spaces[a] += std::to_string(position-1);
    }

    for (int a = 0; a < 5; a++) {
       position = 0;
      word.clear();
        for (int b = 0; b < 5; b++) {
            word.push_back(crossword[b][a]);
        }
         while ( word.find('0',position) == position) {
            position++;
        }


        collumn_white_spaces[a] += std::to_string(position);
        position = word.find('0',position);
        if (position == -1) {
            position = 5;
        }
        
        collumn_white_spaces[a] += std::to_string(position-1);

    }



}

void export_crossword(){
    for (int i = 0; i < 5; i++) {
        for (int a  =0; a < 5; a++) {
            if (crossword[i][a] == ' ') {
                crossword[i][a]= '0';
            }
        }
    }

    writeList(crossword);
    int index = 0;
    std::string word{""};
    for (int i = 0; i < 5; i++) {
        word.clear();
        for (int a = 0; a < 5; a++) {
            if (crossword[i][a] != '0') {
                word.push_back(crossword[i][a]);
            }
        }

        if (Words_Lists_Begginings_And_Ends[0].size() != 0) {
            index = find_in_list(Words_Lists[0],word,Words_Lists_Begginings_And_Ends[0][(word[0]-'A')*2],Words_Lists_Begginings_And_Ends[0][(word[0]-'A')*2+1]);
        } else {
            index = -1;
        }
        
        if (index != -1) { 
            Outo << user_crossword_clues[index] << std::endl;
        } else{
            Outo << "" << std::endl;
        }
    }
    word.clear();
    for (c = 0; c < 5; c++) {
        make_collumn_string(word);
        if (Words_Lists_Begginings_And_Ends[0].size() != 0) {
            index = find_in_list(Words_Lists[0],word,Words_Lists_Begginings_And_Ends[0][(word[0]-'A')*2],Words_Lists_Begginings_And_Ends[0][(word[0]-'A')*2+1]);
        } else {
            index = -1;
        }
        if (index != -1) {
            Outo << user_crossword_clues[index] << std::endl;
        } else{
            Outo << "" << std::endl;
        }
        word.clear();
    }
}


int main() {
    std::cin >> relativePath;
    if (Os == "Linux") {
        relativePath =  relativePath + "/lib/RandomCrosswordGenerator";
        Outo.open(relativePath+"/minicrossword.txt") ;
    } else {
        relativePath = relativePath + + "\\lib\\RandomCrosswordGenerator";
        Outo.open(relativePath+"\\minicrossword.txt") ;
    }
    srand(time(NULL)); // sets the random number generator to output number every time
    make_word_lists();
    add_user_words();
    make_input_crossword();
   make_random_black_squares(); 
    make_third_collumn();  // I chose to make the third collumn prematurely becuase it helps the computers rule out non-possible combinations quicker
   int index = 0;

   for ( r = 0; r < 5; r++) {



        if (how_many_times_didnt_fit > 50000) {
            how_many_times_didnt_fit = 0;
            make_input_crossword();
            make_random_black_squares();
            make_third_collumn();

            r = 0;
        }

        if (!onlyUserWords) {
            index = rows_white_spaces[r][1]-rows_white_spaces[r][0]-1;
        }

        make_row(Words_Lists[index]);


    } 
    export_crossword(); 

    Into.close();
    Outo.close();
    return 0;
}