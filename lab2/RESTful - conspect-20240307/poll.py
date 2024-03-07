from fastapi import FastAPI


app=FastAPI( )


class Poll:

    def __init__(self, name):
        self.name = name
        self.options = dict()
        self.votes = 0

    def add_option(self, name):
        option = {name : 0}
        self.options.update(option)

    def vote(self, option):
        self.options[option] += 1
        self.votes += 1

    def display_results(self):
        return {"name": self.name, 
                "results": self.options}
    
    def display_stats(self):        
        stats = self.options.copy()
        if self.votes != 0:
            for key in stats:
                print(self.votes)
                stats[key] /= self.votes
        return {"name": self.name, 
                "results": self.stats, 
                "number of votes": self.votes}


polls = dict()


@app.get("/")
async def display_polls() :
    return {"polls": list(polls.keys())}

@app.get("/{name}")
async def display_poll(name):
    try:
        return polls[name].display_results()
    except KeyError:
        poll = {name : Poll(name)}
        polls.update(poll)
        return polls[name].display_results()


@app.get("/{poll_name}/option/{option_name}")
async def add_option(poll_name: str, option_name: str) :
    try:
        polls[poll_name].add_option(option_name)
        return {"message": "Option saved!"}
    except KeyError:
        return {"message": "Invalid poll"}
    

@app.get("/{poll_name}/stats")
async def vote(poll_name: str) :
    try:
        return polls[poll_name].display_stats()

    except KeyError:
        return {"message": "Invalid poll"}

@app.get("/{poll_name}/vote/{option_name}")
async def vote(poll_name: str, option_name: str) :
    try:
        polls[poll_name].vote(option_name)
        return {"message": "Vote saved!"}
    except KeyError:
        return {"message": "Invalid option"}
    
    


