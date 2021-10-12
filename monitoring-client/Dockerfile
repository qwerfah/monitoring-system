FROM node:13.8.0-alpine
WORKDIR /app
COPY package.json .
RUN npm config set strict-ssl false
RUN npm install
COPY . .
EXPOSE 4200 49153
CMD npm run start
