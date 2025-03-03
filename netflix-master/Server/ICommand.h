#ifndef ICOMMAND_H
#define ICOMMAND_H

#include <string>
using namespace std;
class ICommand {
public:
    virtual ~ICommand() {}
    virtual string execute(const string& args) = 0; // Accept arguments as a string
};

#endif // ICOMMAND_H
