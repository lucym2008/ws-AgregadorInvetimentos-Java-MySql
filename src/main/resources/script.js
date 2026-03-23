const API_URL_USERS = 'http://localhost:8080/v1/users';
const API_URL_ACCOUNTS = 'http://localhost:8080/v1/accounts';
const API_URL_STOCKS = 'http://localhost:8080/v1/stocks';

let currentUserId = null;
let currentAccountId = null;

// ========== UTILIDADES ==========
function showMessage(msg, type) {
    const messageArea = document.getElementById('messageArea');
    const msgDiv = document.createElement('div');
    msgDiv.className = `message ${type}`;
    msgDiv.innerText = msg;
    messageArea.appendChild(msgDiv);
    setTimeout(() => msgDiv.remove(), 3000);
}

// ========== USUÁRIOS ==========
document.getElementById('createUserForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    const username = document.getElementById('createUsername').value;
    const email = document.getElementById('createEmail').value;
    const password = document.getElementById('createPassword').value;

    try {
        const response = await fetch(API_URL_USERS, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username, email, password })
        });
        if (response.status === 201) {
            showMessage('✅ Usuário criado!', 'success');
            document.getElementById('createUserForm').reset();
            loadUsers();
        } else {
            showMessage('❌ Erro ao criar usuário', 'error');
        }
    } catch (error) {
        showMessage('❌ Erro de conexão', 'error');
    }
});

document.getElementById('loadUsersBtn').addEventListener('click', loadUsers);

async function loadUsers() {
    try {
        const response = await fetch(API_URL_USERS);
        const users = await response.json();
        const userList = document.getElementById('userList');
        userList.innerHTML = '';
        if (users.length === 0) {
            userList.innerHTML = '<li>Nenhum usuário</li>';
            return;
        }
        users.forEach(user => {
            const li = document.createElement('li');
            li.innerHTML = `
                <span><strong>${user.username}</strong> - ${user.email}</span>
                <button onclick="selectUser('${user.userId}', '${user.username}')">Selecionar</button>
            `;
            userList.appendChild(li);
        });
    } catch (error) {
        showMessage('❌ Erro ao carregar usuários', 'error');
    }
}

window.selectUser = async (userId, username) => {
    currentUserId = userId;
    document.getElementById('selectedUserName').innerText = username;
    document.getElementById('userDetailsSection').style.display = 'block';
    document.getElementById('accountsSection').style.display = 'block';

    // Carrega detalhes do usuário
    try {
        const response = await fetch(`${API_URL_USERS}/${userId}`);
        const user = await response.json();
        document.getElementById('userDetails').innerHTML = `
            <p><strong>ID:</strong> ${user.userId}</p>
            <p><strong>Usuário:</strong> ${user.username}</p>
            <p><strong>E-mail:</strong> ${user.email}</p>
        `;
        document.getElementById('updateUsername').value = user.username;
    } catch (error) {
        showMessage('Erro ao carregar detalhes', 'error');
    }

    loadAccounts();
};

// Atualizar usuário
document.getElementById('updateUserForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    if (!currentUserId) return;

    const updateData = {};
    const username = document.getElementById('updateUsername').value;
    const password = document.getElementById('updatePassword').value;
    if (username) updateData.username = username;
    if (password) updateData.password = password;

    if (Object.keys(updateData).length === 0) {
        showMessage('Preencha pelo menos um campo', 'warning');
        return;
    }

    try {
        const response = await fetch(`${API_URL_USERS}/${currentUserId}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(updateData)
        });
        if (response.status === 204) {
            showMessage('✅ Usuário atualizado!', 'success');
            loadUsers();
            selectUser(currentUserId, document.getElementById('selectedUserName').innerText);
        } else {
            showMessage('❌ Erro ao atualizar', 'error');
        }
    } catch (error) {
        showMessage('Erro de conexão', 'error');
    }
});

// Deletar usuário
document.getElementById('deleteUserBtn').addEventListener('click', async () => {
    if (!currentUserId || !confirm('Deletar este usuário?')) return;
    try {
        const response = await fetch(`${API_URL_USERS}/${currentUserId}`, { method: 'DELETE' });
        if (response.status === 204) {
            showMessage('✅ Usuário deletado!', 'success');
            document.getElementById('userDetailsSection').style.display = 'none';
            document.getElementById('accountsSection').style.display = 'none';
            document.getElementById('selectedAccountSection').style.display = 'none';
            currentUserId = null;
            loadUsers();
        } else {
            showMessage('❌ Erro ao deletar', 'error');
        }
    } catch (error) {
        showMessage('Erro de conexão', 'error');
    }
});

document.getElementById('closeDetailsBtn').addEventListener('click', () => {
    document.getElementById('userDetailsSection').style.display = 'none';
    document.getElementById('accountsSection').style.display = 'none';
    document.getElementById('selectedAccountSection').style.display = 'none';
    currentUserId = null;
    currentAccountId = null;
});

// ========== CONTAS ==========
async function loadAccounts() {
    if (!currentUserId) return;
    try {
        const response = await fetch(`${API_URL_USERS}/${currentUserId}/accounts`);
        const accounts = await response.json();
        const accountList = document.getElementById('accountList');
        accountList.innerHTML = '';
        if (accounts.length === 0) {
            accountList.innerHTML = '<li>Nenhuma conta</li>';
            return;
        }
        accounts.forEach(account => {
            const li = document.createElement('li');
            li.innerHTML = `
                <span><strong>${account.description}</strong> (ID: ${account.accountId})</span>
                <button onclick="selectAccount('${account.accountId}')">Selecionar</button>
            `;
            accountList.appendChild(li);
        });
    } catch (error) {
        showMessage('Erro ao carregar contas', 'error');
    }
}

document.getElementById('loadAccountsBtn').addEventListener('click', loadAccounts);

document.getElementById('createAccountForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    if (!currentUserId) return;

    const description = document.getElementById('accountDescription').value;
    const street = document.getElementById('accountStreet').value;
    const number = parseInt(document.getElementById('accountNumber').value);

    try {
        const response = await fetch(`${API_URL_USERS}/${currentUserId}/accounts`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ description, street, number })
        });
        if (response.status === 200) {
            showMessage('✅ Conta criada!', 'success');
            document.getElementById('createAccountForm').reset();
            loadAccounts();
        } else {
            showMessage('❌ Erro ao criar conta', 'error');
        }
    } catch (error) {
        showMessage('Erro de conexão', 'error');
    }
});

// ========== AÇÕES (STOCKS) ==========
window.selectAccount = (accountId) => {
    if (!accountId || accountId === 'undefined') {
        showMessage('ID da conta inválido', 'error');
        return;
    }
    currentAccountId = accountId;
    document.getElementById('selectedAccountId').innerText = accountId;
    document.getElementById('selectedAccountSection').style.display = 'block';
    loadStocks();
};

async function loadStocks() {
    if (!currentAccountId || currentAccountId === 'undefined') {
        console.log('Nenhuma conta selecionada');
        return;
    }
    try {
        const response = await fetch(`${API_URL_ACCOUNTS}/${currentAccountId}/stocks`);
        // ... resto do código
    } catch (error) {
        console.error('Erro ao carregar ações:', error);
    }
}

document.getElementById('loadStocksBtn').addEventListener('click', loadStocks);

document.getElementById('associateStockForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    if (!currentAccountId) return;

    const stockId = document.getElementById('stockId').value;
    const quantity = parseInt(document.getElementById('stockQuantity').value);

    try {
        const response = await fetch(`${API_URL_ACCOUNTS}/${currentAccountId}/stocks`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ stockId, quantity })
        });
        if (response.status === 200) {
            showMessage('✅ Ação associada!', 'success');
            document.getElementById('associateStockForm').reset();
            loadStocks();
        } else {
            showMessage('❌ Erro ao associar ação', 'error');
        }
    } catch (error) {
        showMessage('Erro de conexão', 'error');
    }
});

// ========== CRIAR AÇÃO (STOCK) ==========
document.getElementById('createStockForm').addEventListener('submit', async (e) => {
    e.preventDefault();

    const stockId = document.getElementById('stockIdInput').value;
    const description = document.getElementById('stockDescription').value;

    try {
        const response = await fetch(API_URL_STOCKS, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ stockId, description })
        });
        if (response.status === 200) {
            showMessage('✅ Ação criada!', 'success');
            document.getElementById('createStockForm').reset();
        } else {
            showMessage('❌ Erro ao criar ação', 'error');
        }
    } catch (error) {
        showMessage('Erro de conexão', 'error');
    }
});

// Carrega usuários na inicialização
loadUsers();