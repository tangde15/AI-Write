FROM node:20-alpine
WORKDIR /app

# 仅复制包管理文件以优化缓存
COPY package.json package-lock.json* yarn.lock* pnpm-lock.yaml* ./
RUN npm ci || npm install

# 复制源码
COPY . .

# 让 Vite 监听 0.0.0.0，容器外可访问
ENV VITE_BACKEND_URL=http://backend:8080
EXPOSE 5173
CMD ["npm", "run", "dev", "--", "--host", "0.0.0.0"]
