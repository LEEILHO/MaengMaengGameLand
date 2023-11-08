export const SOCKET_URL = 'http://maengland.com:9200/maeng'
export const KAKAO_REDIRECT_URL = 'https://maengland.com/login/kakao'
export const KAKAO_AUTH_URL = `https://kauth.kakao.com/oauth/authorize?client_id=${process.env.NEXT_PUBLIC_KAKAO_CLIENT_ID}&redirect_uri=${KAKAO_REDIRECT_URL}&response_type=code`
export const SERVER_BASE_URL = 'https://maengland.com/api/v1/'
// http://localhost:3000/login/kakao
// 'https://maengland.com/login/kakao'
