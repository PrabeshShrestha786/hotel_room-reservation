

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

Push your image & show tags on Hub

docker login
docker push e2305278/hotel-room-reservation:0.1.0
docker tag e2305278/hotel-room-reservation:0.1.0 e2305278/hotel-room-reservation:latest
docker push e2305278/hotel-room-reservation:latest


Have a teammate pull, run, edit, bump tag, and push

docker pull e2305278/hotel-room-reservation:0.1.0
docker run --rm -it e2305278/hotel-room-reservation:0.1.0

# after their code change
docker build -t e2305278/hotel-room-reservation:0.1.1 .
docker push e2305278/hotel-room-reservation:0.1.1