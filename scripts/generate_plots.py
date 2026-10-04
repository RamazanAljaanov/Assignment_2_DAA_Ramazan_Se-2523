import os
import pandas as pd
import matplotlib.pyplot as plt

os.makedirs("results/plots", exist_ok=True)
plt.style.use('seaborn-v0_8-whitegrid' if 'seaborn-v0_8-whitegrid' in plt.style.available else 'default')

def plot_workloads():
    csv_file = "results/results.csv"
    if not os.path.exists(csv_file) or os.path.getsize(csv_file) < 10:
        return

    try:
        df = pd.read_csv(csv_file)
    except Exception:
        return

    df_w1 = df[df['workload'] == 'W1']
    if not df_w1.empty:
        plt.figure(figsize=(8, 5))
        for struct in df_w1['structure'].unique():
            sub = df_w1[df_w1['structure'] == struct]
            plt.plot(sub['n'], sub['time_ms'], marker='o', linewidth=2, label=struct)
        plt.title('W1: Random Access (10,000 get calls) - Execution Time vs n', fontsize=13, fontweight='bold')
        plt.xlabel('Number of Elements (n)', fontsize=11)
        plt.ylabel('Execution Time (ms)', fontsize=11)
        plt.xscale('log')
        plt.yscale('log')
        plt.grid(True, which="both", ls="--", alpha=0.5)
        plt.legend(fontsize=11)
        plt.tight_layout()
        plt.savefig('results/plots/w1_random_access_time.png', dpi=300)
        plt.close()

        plt.figure(figsize=(8, 5))
        for struct in df_w1['structure'].unique():
            sub = df_w1[df_w1['structure'] == struct]
            plt.plot(sub['n'], sub['steps'], marker='s', linewidth=2, label=f"{struct} (Steps)")
        plt.title('W1: Random Access - Physical Read Steps vs n', fontsize=13, fontweight='bold')
        plt.xlabel('Number of Elements (n)', fontsize=11)
        plt.ylabel('Total Steps', fontsize=11)
        plt.xscale('log')
        plt.yscale('log')
        plt.grid(True, which="both", ls="--", alpha=0.5)
        plt.legend(fontsize=11)
        plt.tight_layout()
        plt.savefig('results/plots/w1_random_access_ops.png', dpi=300)
        plt.close()

    df_w2 = df[df['workload'] == 'W2']
    if not df_w2.empty:
        plt.figure(figsize=(8, 5))
        for struct in df_w2['structure'].unique():
            sub = df_w2[df_w2['structure'] == struct]
            plt.plot(sub['n'], sub['time_ms'], marker='o', linewidth=2, label=struct)
        plt.title('W2: Search (1,000 contains queries) - Execution Time vs n', fontsize=13, fontweight='bold')
        plt.xlabel('Number of Elements (n)', fontsize=11)
        plt.ylabel('Execution Time (ms)', fontsize=11)
        plt.xscale('log')
        plt.yscale('log')
        plt.grid(True, which="both", ls="--", alpha=0.5)
        plt.legend(fontsize=11)
        plt.tight_layout()
        plt.savefig('results/plots/w2_search_time.png', dpi=300)
        plt.close()

        plt.figure(figsize=(8, 5))
        for struct in df_w2['structure'].unique():
            sub = df_w2[df_w2['structure'] == struct]
            plt.plot(sub['n'], sub['comparisons'], marker='^', linewidth=2, label=f"{struct} (Comparisons)")
            plt.plot(sub['n'], sub['steps'], marker='x', linestyle=':', linewidth=1.5, label=f"{struct} (Steps)")
        plt.title('W2: Search - Steps and Comparisons vs n', fontsize=13, fontweight='bold')
        plt.xlabel('Number of Elements (n)', fontsize=11)
        plt.ylabel('Operation Count', fontsize=11)
        plt.xscale('log')
        plt.yscale('log')
        plt.grid(True, which="both", ls="--", alpha=0.5)
        plt.legend(fontsize=10)
        plt.tight_layout()
        plt.savefig('results/plots/w2_search_ops.png', dpi=300)
        plt.close()

    df_w3_head = df[(df['workload'] == 'W3') & (df['variant'] == 'head')]
    if not df_w3_head.empty:
        plt.figure(figsize=(8, 5))
        for struct in df_w3_head['structure'].unique():
            sub = df_w3_head[df_w3_head['structure'] == struct]
            plt.plot(sub['n'], sub['time_ms'], marker='o', linewidth=2, label=f"{struct} (Time ms)")
        plt.title('W3 (Head): 1,000 Insert & Remove at Index 0 - Time vs n', fontsize=13, fontweight='bold')
        plt.xlabel('Initial Size (n)', fontsize=11)
        plt.ylabel('Execution Time (ms)', fontsize=11)
        plt.xscale('log')
        plt.yscale('log')
        plt.grid(True, which="both", ls="--", alpha=0.5)
        plt.legend(fontsize=11)
        plt.tight_layout()
        plt.savefig('results/plots/w3_insert_remove_head.png', dpi=300)
        plt.close()

    df_w3_mid = df[(df['workload'] == 'W3') & (df['variant'] == 'middle')]
    if not df_w3_mid.empty:
        plt.figure(figsize=(8, 5))
        for struct in df_w3_mid['structure'].unique():
            sub = df_w3_mid[df_w3_mid['structure'] == struct]
            plt.plot(sub['n'], sub['time_ms'], marker='o', linewidth=2, label=f"{struct} (Time ms)")
        plt.title('W3 (Middle): 1,000 Insert & Remove at Index n/2 - Time vs n', fontsize=13, fontweight='bold')
        plt.xlabel('Initial Size (n)', fontsize=11)
        plt.ylabel('Execution Time (ms)', fontsize=11)
        plt.xscale('log')
        plt.yscale('log')
        plt.grid(True, which="both", ls="--", alpha=0.5)
        plt.legend(fontsize=11)
        plt.tight_layout()
        plt.savefig('results/plots/w3_insert_remove_middle.png', dpi=300)
        plt.close()

    df_w4 = df[df['workload'] == 'W4']
    if not df_w4.empty:
        fig, ax1 = plt.subplots(figsize=(8, 5))
        color = 'tab:blue'
        ax1.set_xlabel('Number of Elements (n)', fontsize=11)
        ax1.set_ylabel('Execution Time (ms)', color=color, fontsize=11)
        ax1.plot(df_w4['n'], df_w4['time_ms'], color=color, marker='o', linewidth=2, label='Time (ms)')
        ax1.tick_params(axis='y', labelcolor=color)
        ax1.set_xscale('log')
        ax1.set_yscale('log')

        ax2 = ax1.twinx()
        color = 'tab:red'
        ax2.set_ylabel('Comparisons / Moves', color=color, fontsize=11)
        ax2.plot(df_w4['n'], df_w4['comparisons'], color=color, marker='^', linestyle='--', linewidth=2, label='Comparisons')
        ax2.plot(df_w4['n'], df_w4['moves'], color='tab:green', marker='s', linestyle=':', linewidth=2, label='Moves')
        ax2.tick_params(axis='y', labelcolor=color)
        ax2.set_yscale('log')

        plt.title('W4: MinHeap Priority Processing (n inserts + n extractMin) vs n', fontsize=13, fontweight='bold')
        fig.tight_layout()
        plt.savefig('results/plots/w4_minheap_processing.png', dpi=300)
        plt.close()

    print("[SUCCESS] Workload plots generated successfully in results/plots/.")

def plot_memory():
    mem_file = "results/memory_footprint.csv"
    if not os.path.exists(mem_file) or os.path.getsize(mem_file) < 10:
        return
    try:
        df_mem = pd.read_csv(mem_file)
        plt.figure(figsize=(8, 5))
        for struct in df_mem['structure'].unique():
            sub = df_mem[df_mem['structure'] == struct]
            plt.plot(sub['n'], sub['total_mb'], marker='o', linewidth=2, label=struct)
        plt.title('Bonus Task A: Memory Footprint (JOL) vs n', fontsize=13, fontweight='bold')
        plt.xlabel('Number of Elements (n)', fontsize=11)
        plt.ylabel('Deep Heap Memory (MB)', fontsize=11)
        plt.xscale('log')
        plt.yscale('log')
        plt.grid(True, which="both", ls="--", alpha=0.5)
        plt.legend(fontsize=11)
        plt.tight_layout()
        plt.savefig('results/plots/bonus_memory_jol.png', dpi=300)
        plt.close()
        print("[SUCCESS] Memory plot generated: results/plots/bonus_memory_jol.png")
    except Exception as e:
        print("Skipping memory plot:", e)

if __name__ == '__main__':
    plot_workloads()
    plot_memory()

