#ifndef HELPCOMMAND_H
#define HELPCOMMAND_H

#include <vector>
#include <string>
#include "ICommand.h"

using namespace std;


class HelpCommand : public ICommand {
private:
    vector<string> commands;
public:
    HelpCommand(const vector<string>& commands);

    // Override the execute function
    string execute(const string& args) override;
};

#endif // HELPCOMMAND_H
