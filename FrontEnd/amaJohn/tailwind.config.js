/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ['./src/**/*.{html,ts}'],
  theme: {
    extend: {

      colors: {
        primary: "#D3E8EF",
        secondary: "#7EC6A8",
        cardbackground: "#FAFAF6",
        selectbuttoncolor: "#426B1F",
      },
      fontfamily: {
        newsreader: ["Newsreader", "serif"]
      }















    },
  },
  plugins: [require('@tailwindcss/aspect-ratio')
,require('@tailwindcss/forms')
,require('@tailwindcss/line-clamp')
,require('@tailwindcss/typography')
],
};
