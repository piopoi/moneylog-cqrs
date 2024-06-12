// 데이터베이스와 사용자 삭제
db = db.getSiblingDB('admin');

// 사용자 삭제
db.system.users.remove({ user: "moneylog_cqrs", db: "moneylog_cqrs" });

// 데이터베이스 삭제
db = db.getSiblingDB('moneylog_cqrs');
db.dropDatabase();

// 데이터베이스와 사용자 생성
db = db.getSiblingDB('moneylog_cqrs');

// 사용자 생성
db.createUser({
    user: "moneylog_cqrs",
    pwd: "password",
    roles: [
        { role: "readWrite", db: "moneylog_cqrs" }
    ]
});

// 초기 데이터 삽입 (옵션)
// db.mycollection.insertMany([
//     { name: "Alice", age: 25 },
//     { name: "Bob", age: 30 },
//     { name: "Charlie", age: 35 }
// ]);
