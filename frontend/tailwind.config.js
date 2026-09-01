/** @type {import('tailwindcss').Config} */
export default {
  content: ['./index.html', './src/**/*.{js,jsx}'],
  theme: {
    extend: {
      colors: {
        brand: {
          50: '#FFF1F0', 100: '#FFDFDC', 200: '#FFC4BF', 300: '#FF9A93',
          400: '#F76A60', 500: '#E84A3D', 600: '#D43325', 700: '#B0271D',
          800: '#8F231C', 900: '#76211C'
        }
      },
      fontFamily: {
        sans: ['Inter', 'system-ui', 'Segoe UI', 'Roboto', 'sans-serif']
      }
    }
  },
  plugins: []
}
