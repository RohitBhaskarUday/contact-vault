/** @type {import('tailwindcss').Config} */
export default {
    content: [
        "./src/main/resources/**/*.{html,js}", // ← adjust based on your folder structure
        "./src/main/resources/templates/**/*.html", // if using Thymeleaf
    ],
    darkMode: 'class', // 'media' or 'class'
    theme: {
        extend: {
            // You can extend colors, fonts, etc.
        },
    },
    plugins: [],
}
