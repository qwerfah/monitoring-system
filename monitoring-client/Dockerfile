FROM node:14
WORKDIR /app
COPY package.json .

RUN npm config set strict-ssl false
RUN npm i -D typescript@4.2.3
RUN npm install

COPY . .
EXPOSE 4200 49153
CMD npm run start
