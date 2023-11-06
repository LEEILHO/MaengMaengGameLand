/// <reference lib="webworker" />

// eslint-disable-next-line no-undef
export declare const self: ServiceWorkerGlobalScope

self.addEventListener('install', (e) => {
  console.log('👀 - install', e)
  e.waitUntil(self.skipWaiting())
})

self.addEventListener('activate', (e) => {
  console.log('👀 - activate', e)
})

self.addEventListener('replace', (e) => {
  const message = e.data?.json()
  console.log('👀 - message', message)

  e.waitUntil(
    self.registration.showNotification(message.sender.nickname, {
      body: message.content,
      icon: message.sender.imageUrl,
      data: message.url,
    }),
  )
})

self.addEventListener('notificationclick', (e) => {
  self.clients.openWindow(e.notification.data)
})

// listen to message event from window
self.addEventListener('message', (e) => {
  // HOW TO TEST THIS?
  // Run this in your browser console:
  //     window.navigator.serviceWorker.controller.postMessage({command: 'log', message: 'hello world'})
  // OR use next-pwa injected workbox object
  //     window.workbox.messageSW({command: 'log', message: 'hello world'})
  console.log(e.data)
})
