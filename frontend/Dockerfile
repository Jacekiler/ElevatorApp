# ---------- BUILD ----------
FROM node:20-alpine AS build

WORKDIR /app

COPY package*.json ./
RUN npm install

COPY . .
RUN npm run build -- --configuration production


# ---------- RUNTIME ----------
FROM node:20-alpine

WORKDIR /app

# prosty http server
RUN npm install -g http-server

# kopiujemy builda
COPY --from=build /app/dist/frontend/browser /app/dist

COPY entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh

EXPOSE 8080

ENTRYPOINT ["/entrypoint.sh"]
