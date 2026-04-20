FROM nginx:alpine

# Copy Angular build output
COPY dist/trade-monitor-ui/browser /usr/share/nginx/html

# Nginx config for routing
COPY nginx.conf /etc/nginx/conf.d/default.conf