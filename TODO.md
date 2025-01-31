# Project 2 - Integrity

**This should be viewed in VSC, it looks crusty otherwise, and will not be formatted for viewing in Github**

## TODO LIST

### Tasks

Status marker. x on completion, empty otherwise
|   Task name
|   |                 Assignee (volunteer please!)
V   V                 V
[ ] Something to do > Joe Schmoe


Server:
Needs to store its own name per object
Needs to store a reference to what clients it holds
Needs to be able to send "traffic" to its clients
Get status of all servers
    Number of clients

Clients:
Need to store their own name
Store whether they are a client to a specific server

Methods:
Force a client onto a server
Distribute client onto an empty server
Remove a client from a server
    Disconnect the client and redistribute it ("lost connection")
    Remove client outright
    Nuke a server and redistribute its clients

Misc:
Static counter for number of servers and clients created
Need to make UI to add/remove servers/clients
    Prompt to add server(s)
    Prompt to add user(s)
        Auto assign
        Pick x to send to y server
    Prompt to kill server
    Prompt to pull users?