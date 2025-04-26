let currentUser = null;

document.addEventListener('DOMContentLoaded', () => {
    checkLoginStatus();
});

// 检查登录状态
function checkLoginStatus() {
    const token = localStorage.getItem('token');
    if (token) {
        fetch('http://localhost:80/final_war/api/user/current', {
            headers: { 'Authorization': token }
        })
            .then(res => res.json())
            .then(user => {
                currentUser = user;
                showMainContent();
                loadPosts();
            })
            .catch(() => logout());
    }
}


function login() {
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;

    if (!username || !password) {
        alert('用户名和密码不能为空');
        return;
    }

    fetch('http://localhost:80/final_war/api/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json; charset=UTF-8' },
        body: JSON.stringify({ username:document.getElementById('username'), password:document.getElementById('password') })
    })
        .then(res => {
            if (!res.ok) {
                return res.json().then(err => Promise.reject(err));
            }
            return res.json();
        })
        .then(data => {
            if (data.success) {
                // 根据 LoginServlet 的实际返回数据结构调整
                const sessionId = data.sessionId; // 假设返回的是 sessionId
                localStorage.setItem('token', sessionId);
                currentUser = data.user;
                showMainContent();
                loadPosts();
            } else {
                alert('登录失败: ' + (data.message || '用户名或密码错误'));
            }
        })
        .catch(err => {
            console.error('登录请求失败:', err);
            alert('登录失败: ' + (err.message || '网络错误'));
        });
}
// function login() {
//     const username = document.getElementById('username').value.trim();
//     const password = document.getElementById('password').value.trim();
//
//     console.log("提交登录：", { username, password }); // 调试输出
//
//     fetch('http://localhost:80/final_war/api/login', {
//         method: 'POST',
//         headers: {
//             'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
//         },
//         body: `username=${encodeURIComponent(username)}&password=${encodeURIComponent(password)}`
//     })
//         .then(async response => {
//             console.log("响应状态码：", response.status); // 调试
//             const data = await response.json();
//             if (!response.ok) throw new Error(data.message || '登录失败');
//             return data;
//         })
//         .then(data => {
//             if (data.success) {
//                 window.location.href = '/index.html';
//             } else {
//                 alert('登录失败：' + data.message);
//             }
//         })
//         .catch(error => {
//             console.error('完整错误：', error);
//             alert('登录错误：' + error.message);
//         });
// }

// 注册功能
function register() {
    const username = document.getElementById('reg-username').value;
    const password = document.getElementById('reg-password').value;

    fetch('http://localhost:80/final_war/api/register', {  // 确保路径正确
        method: 'POST',      // 引号不可省略
        headers: { 'Content-Type': 'application/json; charset=UTF-8' },
        body: JSON.stringify({ username, password })
    })
        .then(response => {
            if (!response.ok) throw new Error('HTTP错误: ' + response.status);
            return response.json();
        })
        .then(data => {
            if (data.success) {
                alert('注册成功');
            } else {
                alert('注册失败: ' + (data.message || '未知错误'));
            }
        })
        .catch(error => {
            console.error('注册请求失败:', error);
            alert('注册失败: 网络错误');
        });
}

// 发帖功能
function submitPost() {
    const title = document.getElementById('post-title').value;
    const content = document.getElementById('post-content').value;

    if (!title || !content) {
        alert('标题和内容不能为空');
        return;
    }

    fetch('http://localhost:80/final_war/api/posts', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json; charset=UTF-8' },
        body: JSON.stringify({ title, content })
    })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                alert('发帖成功！帖子ID: ' + data.postId);
                hidePostForm();
                loadPosts(); // 刷新帖子列表
            } else {
                alert('发帖失败: ' + data.message);
            }
        })
        .catch(error => {
            console.error('发帖错误:', error);
            alert('发帖请求失败');
        });
}

// 点赞功能
function toggleLike(postId) {
    fetch('http://localhost:80/final_war/api/like', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json; charset=UTF-8',
            'Authorization': localStorage.getItem('token')
        },
        body: JSON.stringify({
            userId: currentUser.id,
            postId: postId
        })
    })
        .then(res => res.json())
        .then(data => {
            if (data.success) {
                loadPosts(); // 刷新帖子列表
            }
        })
        .catch(err => console.error('点赞失败:', err));
}

// 加载帖子列表
function loadPosts() {
    fetch('http://localhost:80/final_war/api/posts')
        .then(res => res.json())
        .then(posts => {
            const container = document.getElementById('posts-container');
            container.innerHTML = '';

            posts.forEach(post => {
                const postEl = document.createElement('div');
                postEl.className = 'post';
                postEl.innerHTML = `
                <h3>${post.title}</h3>
                <p>${post.content}</p >
                <p>作者: ${post.author.username} | 点赞: ${post.likes}</p >
                <button onclick="toggleLike(${post.id})">
                    ${post.isLiked ? '取消点赞' : '点赞'}
                </button>
            `;
                container.appendChild(postEl);
            });
        })
        .catch(err => console.error('加载帖子失败:', err));
}

// 退出登录
function logout() {
    localStorage.removeItem('token');
    currentUser = null;
    document.getElementById('auth-area').classList.remove('hidden');
    document.getElementById('main-content').classList.add('hidden');
}

// 显示/隐藏辅助函数
function showMainContent() {
    document.getElementById('auth-area').classList.add('hidden');
    document.getElementById('register-area').classList.add('hidden');
    document.getElementById('main-content').classList.remove('hidden');
}

function showRegister() {
    document.getElementById('auth-area').classList.add('hidden');
    document.getElementById('register-area').classList.remove('hidden');
}

function hideRegister() {
    document.getElementById('auth-area').classList.remove('hidden');
    document.getElementById('register-area').classList.add('hidden');
}

function showPostForm() {
    document.getElementById('post-form').classList.remove('hidden');
}

function hidePostForm() {
    document.getElementById('post-form').classList.add('hidden');
    document.getElementById('post-title').value = '';
    document.getElementById('post-content').value = '';
}