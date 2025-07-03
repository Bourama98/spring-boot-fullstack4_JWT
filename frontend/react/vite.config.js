import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    allowedHosts:[
        'my-api2-env.eba-pd3scz3q.us-west-2.elasticbeanstalk.com',
        'http://my-api-env.eba-pd3scz3q.us-west-2.elasticbeanstalk.com:8080',
        ]
  }
})
