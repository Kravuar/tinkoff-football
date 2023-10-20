/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      maxWidth: {
        page: "1104px"
      },
      minWidth: {
        page: "1104px"
      },
      colors: {
        gray: "#f6f7f8",
        ambient: "#f6f7f8"
      }
    },
  },
  plugins: [],
}

