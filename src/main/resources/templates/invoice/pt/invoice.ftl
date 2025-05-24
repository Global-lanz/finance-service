<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <title>Fatura</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            padding: 20px;
            margin: 0;
        }
        .container {
            max-width: 700px;
            margin: auto;
            background: white;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0,0,0,0.05);
        }
        h1 {
            color: #333;
        }
        p, th, td {
            color: #555;
            font-size: 14px;
        }
        .invoice-header, .invoice-footer {
            margin-bottom: 20px;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        th {
            background-color: #f0f0f0;
            padding: 10px;
            text-align: left;
        }
        td {
            padding: 10px;
            border-bottom: 1px solid #ddd;
        }
        .total {
            text-align: right;
            font-size: 16px;
            font-weight: bold;
        }
        .footer {
            font-size: 12px;
            color: #888;
            margin-top: 30px;
            text-align: center;
        }
    </style>
</head>
<body>
<div class="container">
    <div class="invoice-header">
        <h1>Fatura Nª ${invoiceNumber}</h1>
        <p><strong>Cliente:</strong> ${customer}</p>
        <p><strong>Contrato:</strong> ${contractId}</p>
        <p><strong>Data de Vencimento:</strong> ${dueDate}</p>
    </div>

    <table>
        <thead>
        <tr>
            <th>Descrição</th>
            <th>Valor Unitário</th>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td>${description}</td>
            <td>${currency} ${total}</td>
        </tr>
        </tbody>
    </table>

    <p class="total">Total: ${currency} ${total}</p>

    <div class="footer">
        Esta fatura foi gerada automaticamente. Entre em contato se tiver dúvidas.<br/>
        © ${.now?string["yyyy"]} Global Lanz Technologies. Todos os direitos reservados.
    </div>
</div>
</body>
</html>
