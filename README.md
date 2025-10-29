# hotel_room-reservation
This is hotel room reservation app.

#normal pull
 git pull origin main

 #if someone else made change to project then you have to pull the base again
 git pull --rebase origin main



 #After code is done.
git add .
git commit -m "your message"
git push origin main

# Make sure you're on main and up to date
git checkout main
git pull origin main

# Create a new branch (for example: feature-login)
git checkout -b feature-login

git add .
git commit -m "Added login feature"

git push -u origin feature-login


#this is a test
#  if we are working on our own branch(feature-signup)
git checkout feature-signup
git merge main

or 
git rebase main

then 

git push
