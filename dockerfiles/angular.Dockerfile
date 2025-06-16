FROM node:22-alpine
WORKDIR /app
COPY frontend/package.json frontend/package-lock.json ./
RUN npm install
COPY frontend/. .
EXPOSE 4200
CMD ["npm", "run", "start"]