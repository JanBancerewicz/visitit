# build
FROM node:20-alpine AS build
WORKDIR /app
COPY package*.json ./
# jeśli brak package-lock.json, zamień na: RUN npm install
RUN npm ci
COPY . .
RUN npm run build -- --configuration=production --output-path=/web

# run
FROM nginx:1.27-alpine
RUN apk add --no-cache bash gettext
COPY --from=build /web/browser/ /usr/share/nginx/html/
COPY nginx/default.conf.template /etc/nginx/templates/default.conf.template
COPY nginx/docker-entrypoint.sh /docker-entrypoint.sh
RUN chmod +x /docker-entrypoint.sh
EXPOSE 80
ENTRYPOINT ["/docker-entrypoint.sh"]
CMD ["nginx","-g","daemon off;"]
