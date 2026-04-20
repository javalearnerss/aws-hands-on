
FROM node:18 AS build
FROM nginx:alpine
COPY --from=build /libs/trade-monitor-ui /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf