# Verwende ein Node.js-Image als Basis
FROM node:18-alpine

# Arbeitsverzeichnis im Container festlegen
WORKDIR /app

# Installiere die Abhängigkeiten
COPY package.json package-lock.json ./
RUN npm install

# Kopiere den gesamten Frontend-Code in den Container
COPY . .

# Baue die React-App
RUN npm run build

# Installiere einen HTTP-Server (z.B. serve), um die gebaute App zu hosten
RUN npm install -g serve

# Stelle den Port ein, auf dem die App läuft
EXPOSE 5000

# Starte die App mit serve
CMD ["serve", "-s", "build", "-l", "5000"]
