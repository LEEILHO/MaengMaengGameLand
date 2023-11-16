// import { getAllPosts } from '@/service/posts'

// export default async function sitemap() {
//   const baseUrl = 'https://pyeongdevlog.vercel.app'

//   const posts = await getAllPosts()
//   const postUrls = posts.map((post) => ({
//     url: `${baseUrl}/blog/${post.path}`,
//     lastModified: post.date,
//   }))
//   return [
//     { url: baseUrl, lastModified: new Date() },
//     { url: `${baseUrl}/blog`, lastModified: new Date() },

//     ...postUrls,
//   ]
// }
