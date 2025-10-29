

# Make sure you're on main and up to date
git checkout main
git pull origin main

# Create a new branch (for example: feature-login)
git checkout -b feature-yourfeature

git add .
git commit -m "Added  yourfeature"

git push -u origin feature-yourfeature

# run the project
mvn clean
mvn javafx:run

